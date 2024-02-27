package de.uol.informatik.marvel.nmea0183.messages;

import de.dlr.s666.gml._0.Angle;
import de.dlr.s666.gml._0.AngleUnit;
import de.dlr.s666.gml._0.DateTime;
import de.dlr.s666.gml._0.InvalidAngle;
import de.dlr.s666.gml._0.PositionNavigationalStatus;
import de.dlr.s666.gml._0.PositioningSystemMode;
import de.dlr.s666.gml._0.Valid;
import de.dlr.s666.gml._0.Vessel;
import de.dlr.s666.gml._0.VesselDynamicCharacteristic;
import de.uol.informatik.marvel.nmea0183.util.N0183DateTimeBuilder;
import de.uol.informatik.marvel.nmea0183.util.N0183DecodeEnumMapper;
import de.uol.informatik.marvel.nmea0183.util.N0183EncodeEnumMapper;
import de.uol.informatik.marvel.nmea0183.util.N0183ExtractUtil;
import de.uol.informatik.marvel.nmea0183.util.N0183MeasureUtil;
import de.uol.informatik.marvel.nmea0183.util.N0183ParserUtil;
import de.uol.informatik.marvel.s666.ComplexUnits;
import de.uol.informatik.marvel.s666.S666;
import de.uol.informatik.marvel.util.DecoderUtils;
import de.uol.informatik.marvel.util.MeasureUtil;
import de.uol.informatik.marvel.util.UnitConversions;
import de.uol.informatik.marvel.util.UnmeasurableUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static de.uol.informatik.marvel.nmea0183.util.N0183ExtractUtil.linkedMapOf;

/**
 * RMC - Recommended minimum specific GNSS data
 *
 * <p>Time, date, position, course and speed data provided by a GNSS navigation receiver.
 *
 * <pre>                                                       12
 *         1        2  3      4  5       6  7   8   9    10  11| 13
 *         |        |  |      |  |       |  |   |   |    |   | | |
 * $--RMC,hhmmss.ss,A,ddmm.mm,a,dddmm.mm,a,x.x,x.x,xxxxx,x.x,a,a,a*hh
 *
 * Field Number:
 * 1) UTC of position fix, hh is hours, mm is minutes, ss.ss is seconds.
 * 2) Status, A = Valid, V = Warning
 * 3) Latitude, dd is degrees. mm.mm is minutes.
 * 4) N or S
 * 5) Longitude, ddd is degrees. mm.mm is minutes.
 * 6) E or W
 * 7) Speed over ground, knots
 * 8) Course over ground, degrees true
 * 9) Date, ddmmyy
 * 10) Magnetic Variation, degrees
 * 11) E = Easterly variation subtracts from True course, W = Westerly variation adds to True course
 * 12) FAA mode indicator (NMEA 2.3 and later) ->
 *     A = Autonomous. Satellite system used in non-differential mode in position fix
 *     D = Differential. Satellite system used in differential mode in position fix
 *     E = Estimated (dead reckoning) mode
 *     F = Float RTK. Satellite system used in real time kinematic mode with floating integers
 *     M = Manual input mode
 *     N = No fix. Satellite system not used in position fix, or fix not valid
 *     P = Precise. Satellite system used in precision mode. Precision mode is defined as: no
 *         deliberate degradation (such as selective availability) and higher resolution code
 *         (P-code) is used to compute position fix. P is also used for satellite system used in
 *         multi-frequency, SBAS or Precise Point Positioning (PPP) mode
 *     R = Real time kinematic. Satellite system used in RTK mode with fixed integers
 *     S = Simulator mode
 * 13) Navigational status (NMEA 4.1 and later)
 *     S = Safe when the estimated positioning accuracy (95 % confidence) is within the selected
 *         accuracy level corresponding to the actual navigation mode, and/or integrity is available
 *         and within the requirements for the actual navigation mode, and/or a new valid position
 *         has been calculated within 1 s for a conventional craft and 0,5 s for a high speed craft.
 *     C = Caution when integrity is not available
 *     U = Unsafe when the estimated positioning accuracy (95 % confidence) is less than the
 *         selected accuracy level corresponding to the actual navigation mode, and/or integrity is
 *         available but exceeds the requirements for the actual navigation mode, and/or a new valid
 *         position has not been calculated within 1 s for a conventional craft and 0,5 s for a high
 *         speed craft
 *     V = Navigational status not valid, equipment is not providing navigational status indication
 *
 * </pre>
 * <p>
 * Example: $ECRMC,101322.19,A,5310.980,N,00832.996,E,1.1,337.7,200922,003.3,E,D,S*57
 * </p>
 * Source: IEC 61162-1
 * <a href="https://www.chenyupeng.com/upload/2020/3/IEC%2061162-1-2010-728ad3778103426ab0a9a64b6cc5e474.pdf">RMC</a>
 * </p>
 */
public class RmcSentence extends Nmea0183Sentence {
	private static final String msgId = "RMC";

	private static final int I_UTC_TIME = 1;
	private static final int I_STATUS = 2;
	private static final int I_LATITUDE = 3;
	private static final int I_LATITUDE_REFERENCE = 4;
	private static final int I_LONGITUDE = 5;
	private static final int I_LONGITUDE_REFERENCE = 6;
	private static final int I_SOG = 7;
	private static final int I_TRACK_MADE_GOOD = 8;
	private static final int I_DATE = 9;
	private static final int I_MAGNETIC_VARIATION = 10;
	private static final int I_MAGNETIC_VARIATION_REFERENCE = 11;
	private static final int I_FAA_MODE_INDICATOR = 12;
	private static final int I_NAV_STATUS = 13;

	@Override
	public void decode(S666 s666, String[] fields) {
		VesselDynamicCharacteristic vesselDynamicCharacteristic =
				new VesselDynamicCharacteristic();

		// SOG
		vesselDynamicCharacteristic.setSpeedOverGround(
				MeasureUtil.makeSpeed(
						fields[I_SOG],
						DecoderUtils::toDecimal,
						ComplexUnits.NAUTICAL_MILES_PER_HOUR,
						Optional::of
				)
		);
		// track made good
		vesselDynamicCharacteristic.setTrack(
				MeasureUtil.makeAngle(
						fields[I_TRACK_MADE_GOOD],
						DecoderUtils::toDecimal,
						AngleUnit.DEGREE,
						Optional::of
				)
		);
		s666.addMeasurableToMetadata(
				new N0183DateTimeBuilder(false)
						.addDate(fields[I_DATE], "ddMMyy")
						.addUtcTime(fields[I_UTC_TIME])
						.makeDateTime()
		);
		// only convert angleMinutes to decimal degree
		// "ddmm.mm" to "dd.dd"
		Optional<BigDecimal> convertedLatitude = DecoderUtils.toDecimal(
				fields[I_LATITUDE]).map(
				lat -> lat.movePointLeft(2)
						// get degrees
						.setScale(0, RoundingMode.DOWN)
						// add minutes converted to decimal degrees
						.add(lat.movePointLeft(2)
								.remainder(BigDecimal.ONE)
								.movePointRight(2)
								.multiply(UnitConversions.MINUTE_TO_DEGREE)
						)
						// set number of fractional digits to that of the actual value
						.setScale(lat.remainder(BigDecimal.ONE).scale(), RoundingMode.DOWN)
		);
		// "dddmm.mm" to "ddd.dd"
		Optional<BigDecimal> convertedLongitude = DecoderUtils.toDecimal(
				fields[I_LONGITUDE]).map(
				lo -> lo.movePointLeft(2)
						// get degrees
						.setScale(0, RoundingMode.DOWN)
						// add minutes converted to decimal degrees
						.add(lo.movePointLeft(2)
								.remainder(BigDecimal.ONE)
								.movePointRight(2)
								.multiply(UnitConversions.MINUTE_TO_DEGREE)
						)
						// set number of fractional digits to that of the actual value
						.setScale(lo.remainder(BigDecimal.ONE).scale(), RoundingMode.DOWN)
		);
		String latitude = fields[I_LATITUDE];
		if (convertedLatitude.isPresent()) {
			latitude = String.valueOf(convertedLatitude.get());
		}
		String longitude = fields[I_LONGITUDE];
		if (convertedLongitude.isPresent()) {
			longitude = String.valueOf(convertedLongitude.get());
		}
		// combine with direction
		var lonFromDirection = MeasureUtil.makeAngleWithDirection(
				longitude, DecoderUtils::toPositiveDecimal,
				fields[I_LONGITUDE_REFERENCE],
				N0183DecodeEnumMapper.S666UnmeasurableValues::mapAngleDirectionEastWest,
				BigDecimal::multiply,
				AngleUnit.DEGREE, Optional::of
		);
		var latFromDirection = MeasureUtil.makeAngleWithDirection(
				latitude, DecoderUtils::toPositiveDecimal,
				fields[I_LATITUDE_REFERENCE],
				N0183DecodeEnumMapper.S666UnmeasurableValues::mapAngleDirectionNorthSouth,
				BigDecimal::multiply,
				AngleUnit.DEGREE, Optional::of
		);
		// set position
		Vessel vessel = new Vessel();
		N0183MeasureUtil.makePositionWithLatLong(
				fields[I_LATITUDE], fields[I_LATITUDE_REFERENCE],
				fields[I_LONGITUDE], fields[I_LONGITUDE_REFERENCE]
		).ifPresent(position -> vessel.setPose(MeasureUtil.makePose(Optional.of(position), val -> val,
				null, null).get()));
		// magnetic variation
		if (fields[I_MAGNETIC_VARIATION_REFERENCE].equals("E")
		    || fields[I_MAGNETIC_VARIATION_REFERENCE].equals("W")) {
			Angle magneticVariation = MeasureUtil.makeAngle(
					fields[I_MAGNETIC_VARIATION],
					DecoderUtils::toDecimal,
					AngleUnit.DEGREE,
					Optional::of
			);
			if (fields[I_MAGNETIC_VARIATION_REFERENCE].equals("W")) {
				magneticVariation.setMagnitude(magneticVariation.getMagnitude().negate());
			}
			vesselDynamicCharacteristic.setMagneticVariation(magneticVariation);
		} else {
			vesselDynamicCharacteristic.setMagneticVariation(
					MeasureUtil.makeAngle(
							fields[I_MAGNETIC_VARIATION],
							DecoderUtils::toDecimal,
							AngleUnit.DEGREE,
							Optional::of,
							fields[I_MAGNETIC_VARIATION_REFERENCE],
							ref -> Optional.empty()
					)
			);
		}
		if ((fields.length - 1) > I_FAA_MODE_INDICATOR) {
			// field of NMEA0183 version 2.3 and later
			s666.addUnmeasurableToMetadata(
					UnmeasurableUtil.makePositioningSystemMode(
							fields[I_FAA_MODE_INDICATOR],
							N0183DecodeEnumMapper.S666UnmeasurableValues::mapExtendedPositioningSystemMode
					)

			);
		}
		if ((fields.length - 1) > I_NAV_STATUS) {
			// field of NMEA0183 version 4.1 and later
			s666.addUnmeasurableToMetadata(
					UnmeasurableUtil.makePositionNavigationalStatus(
							fields[I_NAV_STATUS],
							N0183DecodeEnumMapper
									.S666UnmeasurableValues::mapPositionNavigationalStatus
					)

			);
		}
		s666.addMeasurableToMetadata(MeasureUtil.makeStatusReferenceValid(
						fields[I_STATUS],
						N0183DecodeEnumMapper.S666UnmeasurableValues::mapBoolean
				)
		);
		vessel.setVesselDynamicCharacteristics(vesselDynamicCharacteristic);
		s666.addFeatureToResult(vessel);
	}

	/**
	 * Try to generate an RMC Sentence from the data inside the S666 object.
	 *
	 * @param s666 objects that contain the desired measure,
	 * 		f.e. {@link VesselDynamicCharacteristic}
	 * @return a list of generated sentences.
	 */
	@Override
	public List<String> encode(S666 s666) {
		Vessel vessel = s666.getFeature(Vessel.class);
		if (vessel == null) {
			return List.of();
		}
		return List.of(extractRmcContent(s666, vessel));
	}

	/**
	 * Extracts the relevant content.
	 */
	private String extractRmcContent(S666 s666, Vessel vessel) {
		List<String> msgContent = new ArrayList<>();
		N0183ExtractUtil.addTimeToContent(msgContent, s666.getMeasurableMetadata(DateTime.class));

		Valid validStatus = s666.getMeasurableMetadata(Valid.class);
		N0183ExtractUtil.addValidCharToContent(msgContent, validStatus);

		N0183ExtractUtil.addPoseWithDirectionToContent(msgContent, vessel.getPose());

		VesselDynamicCharacteristic vdc =
				vessel.getVesselDynamicCharacteristics();
		N0183ExtractUtil.addPossiblyCombinedSpeedWithUnitToContent(msgContent,
				vdc != null ? vdc.getSpeedOverGround() : null,
				linkedMapOf(ComplexUnits.NAUTICAL_MILES_PER_HOUR, "N")
		);
		msgContent.remove(msgContent.size() - 1);
		N0183ExtractUtil.addAngleToContentWithoutReference(msgContent,
				vdc != null ? vdc.getTrack() : null,
				AngleUnit.DEGREE);
		N0183ExtractUtil.addDateToContentAsCoherentString(msgContent,
				s666.getMeasurableMetadata(DateTime.class));
		Angle magneticVariation = vdc != null ? vdc.getMagneticVariation() : null;
		N0183ExtractUtil.addAngleToContentWithoutReference(msgContent, magneticVariation, AngleUnit.DEGREE);
		if (magneticVariation != null && magneticVariation.getMagnitude() != null) {
			msgContent.add(
					magneticVariation.getMagnitude().compareTo(BigDecimal.ZERO) < 0 ? "W" : "E");
		} else if (magneticVariation != null
				&& ((InvalidAngle) magneticVariation).getInvalidReference() != null) {
			msgContent.add(
					((InvalidAngle) magneticVariation).getInvalidReference());
		} else {
			msgContent.add("");
		}
		PositioningSystemMode faaMode = s666.getUnmeasurableMetadata(PositioningSystemMode.class);
		N0183ExtractUtil.addUnmeasurableToContent(msgContent,
				faaMode,
				UnmeasurableUtil::extractPositioningSystemMode,
				N0183EncodeEnumMapper.S666UnmeasurableValues::mapPositioningSystemMode);

		PositionNavigationalStatus positionNavigationalStatus =
				s666.getUnmeasurableMetadata(PositionNavigationalStatus.class);
		N0183ExtractUtil.addUnmeasurableToContent(msgContent,
				positionNavigationalStatus,
				UnmeasurableUtil::extractPositionNavigationStatus,
				N0183EncodeEnumMapper.S666UnmeasurableValues::mapPositionNavigationalStatus);

		return N0183ParserUtil.createSentence(msgId, msgContent);
	}


	@Override
	public Set<Integer> getLastFieldIndex() {
		return Set.of(I_MAGNETIC_VARIATION_REFERENCE, I_FAA_MODE_INDICATOR, I_NAV_STATUS);
	}
}
