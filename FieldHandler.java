package de.uol.informatik.marvel.util;

import de.dlr.s666.gml._0.AbstractMeasurableType;
import de.dlr.s666.gml._0.AbstractUnmeasurableType;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Collection of static methods, that initialize
 * a {@link AbstractMeasurableType} or {@link AbstractUnmeasurableType}
 * save the information of given message type fields in it and return it.
 */
@SuppressWarnings({ "OptionalUsedAsFieldOrParameterType", "checkstyle:LineLength", "checkstyle:MethodTypeParameterName", "checkstyle:OverloadMethodsDeclarationOrder" })
public class FieldHandler {

	/**
	 * no instances, static members only.
	 */
	private FieldHandler() {
	}

	/**
	 * Initialize the given valid or invalid measure,
	 * interprets the given field with the given parser
	 * and save the resulting interpretation in the valid or invalid measure with the given setters.
	 * An invalid measure will be initialized, if the field or its interpretation is empty
	 * and otherwise a valid measure.
	 *
	 * <p>If you already know that the measure is valid or invalid,
	 * you should use this short form {@link #makeInfallibleMeasure(Supplier, Object, BiConsumer)},
	 * or the other variations with more fields, instead.
	 *
	 * @param newValidMeasure constructor for the valid measure
	 * @param newInvalidMeasure constructor for the invalid measure
	 * @param field field which should be added to the measure
	 * @param parser turn the field into its s666 interpretation; empty optional if invalid
	 * @param setValidValue setter methode to save parsed field in the given valid measure
	 * @param setInvalidValue setter methode to save unparsed field in the given invalid measure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <P> Type in which field should be interpreted
	 * @param <V> Type of the created measure if valid
	 * @param <E> Type of the created measure if invalid
	 * @return the valid or invalid measure
	 */
	public static <F, P, V extends AbstractMeasurableType, E extends V> V makeMeasure(
			Supplier<V> newValidMeasure, Supplier<E> newInvalidMeasure,
			F field, Function<F, Optional<P>> parser,
			BiConsumer<V, P> setValidValue, BiConsumer<E, String> setInvalidValue
	) {
		var result = ResultReference.newMeasurable(newValidMeasure, newInvalidMeasure);

		Optional<P> parsedField = parseField(
				Optional.ofNullable(field),
				parser,
				result
		);

		return setField(
				parsedField, field,
				result,
				setValidValue, setInvalidValue
		);
	}

	/**
	 * Initialize the given valid or invalid measure,
	 * interprets the given field with the given parser
	 * and save the resulting interpretation in the valid or invalid measure with the given setters.
	 * An invalid measure will be initialized, if the field or its interpretation is empty
	 * and otherwise a valid measure.
	 *
	 * <p>If you already know that the measure is valid or invalid,
	 * you should use this short form {@link #makeInfallibleMeasure(Supplier, Object, BiConsumer)},
	 * or the other variations with more fields, instead.
	 *
	 * @param newValidMeasure constructor for the valid measure
	 * @param newInvalidMeasure constructor for the invalid measure
	 * @param firstField field which should be added to the measure
	 * @param fieldOneParser turn the field into its s666 interpretation; empty optional if invalid
	 * @param setValidValueOne setter methode to save parsed field in the given valid measure
	 * @param setInvalidValueOne setter methode to save unparsed field in the given invalid measure
	 * @param secondField field which should be added to the measure
	 * @param fieldTwoParser turn the field into its s666 interpretation; empty optional if invalid
	 * @param setValidValueTwo setter methode to save parsed field in the given valid measure
	 * @param setInvalidValueTwo setter methode to save unparsed field in the given invalid measure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <PF> Type in which field should be interpreted
	 * @param <PS> Type in which field should be interpreted
	 * @param <V> Type of the created measure if valid
	 * @param <E> Type of the created measure if invalid
	 * @return the valid or invalid measure
	 */
	public static <F, S, PF, PS, V extends AbstractMeasurableType, E extends V> V makeMeasure(
			Supplier<V> newValidMeasure, Supplier<E> newInvalidMeasure,
			F firstField, Function<F, Optional<PF>> fieldOneParser,
			BiConsumer<V, PF> setValidValueOne, BiConsumer<E, String> setInvalidValueOne,
			S secondField, Function<S, Optional<PS>> fieldTwoParser,
			BiConsumer<V, PS> setValidValueTwo, BiConsumer<E, String> setInvalidValueTwo
	) {
		var result = ResultReference.newMeasurable(newValidMeasure, newInvalidMeasure);
		Optional<PF> parsedFirst = parseField(
				Optional.ofNullable(firstField),
				fieldOneParser,
				result
		);
		Optional<PS> parsedSecond = parseField(
				Optional.ofNullable(secondField),
				fieldTwoParser,
				result
		);

		setField(
				parsedFirst, firstField,
				result,
				setValidValueOne, setInvalidValueOne
		);
		setField(
				parsedSecond, secondField,
				result,
				setValidValueTwo, setInvalidValueTwo
		);
		return result.get();
	}

	/**
	 * Initialize the given valid or invalid measure,
	 * interprets the given field with the given parser
	 * and save the resulting interpretation in the valid or invalid measure with the given setters.
	 * An invalid measure will be initialized, if the field or its interpretation is empty
	 * and otherwise a valid measure.
	 *
	 * <p>If you already know that the measure is valid or invalid,
	 * you should use this short form {@link #makeInfallibleMeasure(Supplier, Object, BiConsumer)},
	 * or the other variations with more fields, instead.
	 *
	 * @param newValidMeasure constructor for the valid measure
	 * @param newInvalidMeasure constructor for the invalid measure
	 * @param firstField field which should be added to the measure
	 * @param fieldOneParser turn the field into its s666 interpretation; empty optional if invalid
	 * @param setValidValueOne setter methode to save parsed field in the given valid measure
	 * @param setInvalidValueOne setter methode to save unparsed field in the given invalid measure
	 * @param secondField field which should be added to the measure
	 * @param fieldTwoParser turn the field into its s666 interpretation; empty optional if invalid
	 * @param setValidValueTwo setter methode to save parsed field in the given valid measure
	 * @param setInvalidValueTwo setter methode to save unparsed field in the given invalid measure
	 * @param thirdField which should be added to the measure
	 * @param fieldThreeParser turn the field into its s666 interpretation; empty optional if invalid
	 * @param setValidValueThree setter methode to save parsed field in the given valid measure
	 * @param setInvalidValueThree setter methode to save unparsed field in the given invalid measure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <PF> Type in which field should be interpreted
	 * @param <PS> Type in which field should be interpreted
	 * @param <PT> Type in which field should be interpreted
	 * @param <V> Type of the created measure if valid
	 * @param <E> Type of the created measure if invalid
	 * @return the valid or invalid measure
	 */
	public static <F, S, T, PF, PS, PT, V extends AbstractMeasurableType, E extends V> V makeMeasure(
			Supplier<V> newValidMeasure, Supplier<E> newInvalidMeasure,
			F firstField, Function<F, Optional<PF>> fieldOneParser,
			BiConsumer<V, PF> setValidValueOne, BiConsumer<E, String> setInvalidValueOne,
			S secondField, Function<S, Optional<PS>> fieldTwoParser,
			BiConsumer<V, PS> setValidValueTwo, BiConsumer<E, String> setInvalidValueTwo,
			T thirdField, Function<T, Optional<PT>> fieldThreeParser,
			BiConsumer<V, PT> setValidValueThree, BiConsumer<E, String> setInvalidValueThree
	) {
		var result = ResultReference.newMeasurable(newValidMeasure, newInvalidMeasure);
		Optional<PF> parsedFirst = parseField(
				Optional.ofNullable(firstField),
				fieldOneParser,
				result
		);
		Optional<PS> parsedSecond = parseField(
				Optional.ofNullable(secondField),
				fieldTwoParser,
				result
		);
		Optional<PT> parsedThird = parseField(
				Optional.ofNullable(thirdField),
				fieldThreeParser,
				result
		);

		setField(
				parsedFirst, firstField,
				result,
				setValidValueOne, setInvalidValueOne
		);
		setField(
				parsedSecond, secondField,
				result,
				setValidValueTwo, setInvalidValueTwo
		);
		setField(
				parsedThird, thirdField,
				result,
				setValidValueThree, setInvalidValueThree
		);
		return result.get();
	}

	/**
	 * Initialize the given valid or invalid unmeasure,
	 * interprets the given field with the given parser
	 * and save the resulting interpretation in the valid or invalid unmeasure with the given setters.
	 * An invalid unmeasure will be initialized, if the field or its interpretation is empty
	 * and otherwise a valid unmeasure.
	 *
	 * <p>If you already know that the unmeasure is valid or invalid,
	 * you should use this short form {@link #makeInfallibleUnmeasure(Supplier, Object, BiConsumer)},
	 * or the other variations with more fields, instead.
	 *
	 * @param newValidUnmeasure constructor for the valid unmeasure
	 * @param newInvalidUnmeasure constructor for the invalid unmeasure
	 * @param field field which should be added to the unmeasure
	 * @param parser turn the field into its s666 interpretation; empty optional if invalid
	 * @param setValidValue setter methode to save parsed field in the given valid unmeasure
	 * @param setInvalidValue setter methode to save unparsed field in the given invalid unmeasure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <P> Type in which field should be interpreted
	 * @param <V> Type of the created measure if valid
	 * @param <E> Type of the created measure if invalid
	 * @return the valid or invalid unmeasure
	 */
	public static <F, P, V extends AbstractUnmeasurableType, E extends V> V makeUnmeasure(
			Supplier<V> newValidUnmeasure, Supplier<E> newInvalidUnmeasure,
			F field, Function<F, Optional<P>> parser,
			BiConsumer<V, P> setValidValue, BiConsumer<E, String> setInvalidValue
	) {
		var result = ResultReference.newUnmeasurable(newValidUnmeasure, newInvalidUnmeasure);

		Optional<P> parsedField = parseField(
				Optional.ofNullable(field),
				parser,
				result
		);

		return setField(
				parsedField, field,
				result,
				setValidValue, setInvalidValue
		);
	}

	/**
	 * Initialize the given valid or invalid measure,
	 * interprets the given combined fields with the given parsers
	 * and save the resulting interpretation in the valid or invalid measure with the given setters.
	 * An invalid measure will be initialized, if the field or its interpretation is empty
	 * and otherwise a valid measure.
	 *
	 * <p>Only use this if you want to parse the information of two field into one field in S666.
	 * Otherwise, use the standard Function
	 * {@link #makeMeasure(Supplier, Supplier, Object, Function, BiConsumer, BiConsumer)}.
	 *
	 * @param newValidMeasure constructor for the valid measure
	 * @param newInvalidMeasure constructor for the invalid measure
	 * @param field field which should be added to the measure
	 * @param fieldParser prepare the field for the combination; empty optional if invalid
	 * @param fieldInfluence field which should be combined with field
	 * @param fieldInfluenceParser prepare the fieldInfluence for the combination; empty optional if invalid
	 * @param setValidValue setter methode to save parsed field in the given valid measure
	 * @param compoundParser combines the field and fieldInfluence into its s666 interpretation; empty optional if invalid
	 * @param setIndirectInvalidValue setter methode to save parsed field with unparsed fieldInfluence in the given invalid measure
	 * @param setInvalidValue setter methode to save unparsed field in the given invalid measure
	 * @param setIndirectInvalidValueInfluence setter methode to save parsed fieldInfluence with unparsed field in the given invalid measure
	 * @param setInvalidValueInfluence setter methode to save unparsed fieldInfluence in the given invalid measure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <FI> Type of the fieldInfluence
	 * @param <P> Type in which field should be interpreted
	 * @param <PI> Type in which fieldInfluence should be interpreted
	 * @param <CP> Type in which the combination of field and fieldInfluence should be interpreted
	 * @param <V> Type of the created measure if valid
	 * @param <E> Type of the created measure if invalid
	 * @return the valid or invalid measure
	 */
	public static <F, FI, P, PI, CP, V extends AbstractMeasurableType, E extends V> V makeMeasure(
			Supplier<V> newValidMeasure, Supplier<E> newInvalidMeasure,
			F field, Function<F, Optional<P>> fieldParser,
			FI fieldInfluence, Function<FI, Optional<PI>> fieldInfluenceParser,
			BiConsumer<V, CP> setValidValue, BiFunction<P, PI, CP> compoundParser,
			BiConsumer<E, String> setIndirectInvalidValue, BiConsumer<E, String> setInvalidValue,
			BiConsumer<E, String> setIndirectInvalidValueInfluence, BiConsumer<E, String> setInvalidValueInfluence
	) {
		var result = ResultReference.newMeasurable(newValidMeasure, newInvalidMeasure);

		Optional<P> parsedField = parseField(
				Optional.ofNullable(field),
				fieldParser,
				result
		);
		Optional<PI> parsedFieldInfluence = parseField(
				Optional.ofNullable(fieldInfluence),
				fieldInfluenceParser,
				result
		);

		if (parsedField.isEmpty() || parsedFieldInfluence.isEmpty()) {
			return setInvalidCompoundField(
					parsedField, field,
					parsedFieldInfluence, fieldInfluence,
					result,
					setIndirectInvalidValue, setInvalidValue,
					setIndirectInvalidValueInfluence, setInvalidValueInfluence
			);
		} else {
			var compound = compoundParser.apply(parsedField.get(), parsedFieldInfluence.get());
			Objects.requireNonNull(compound, "FieldHandler.makeMeasure(...): The given BiFunction compoundParser returned null. When both input parameter are valid, the function must return the combined value!");
			return makeInfallibleMeasure(
					newValidMeasure,
					compound,
					setValidValue
			);
		}
	}

	/**
	 * Initialize the given valid or invalid measure,
	 * interprets the given combined fields and another normal field with the given parsers
	 * and save the resulting interpretation in the valid or invalid measure with the given setters.
	 * An invalid measure will be initialized, if the field or its interpretation is empty
	 * and otherwise a valid measure.
	 *
	 * <p>Only use this if you want to parse the information of two field into one field in S666.
	 * Otherwise, use the standard Function
	 * {@link #makeMeasure(Supplier, Supplier, Object, Function, BiConsumer, BiConsumer)}.
	 *
	 * @param newValidMeasure constructor for the valid measure
	 * @param newInvalidMeasure constructor for the invalid measure
	 * @param field field which should be added to the measure
	 * @param fieldParser prepare the field for the combination; empty optional if invalid
	 * @param fieldInfluence field which should be combined with field
	 * @param fieldInfluenceParser prepare the fieldInfluence for the combination; empty optional if invalid
	 * @param setValidValue setter methode to save parsed field in the given valid measure
	 * @param compoundParser combines the field and fieldInfluence into its s666 interpretation; empty optional if invalid
	 * @param setIndirectInvalidValue setter methode to save parsed field with unparsed fieldInfluence in the given invalid measure
	 * @param setInvalidValue setter methode to save unparsed field in the given invalid measure
	 * @param setIndirectInvalidValueInfluence setter methode to save parsed fieldInfluence with unparsed field in the given invalid measure
	 * @param setInvalidValueInfluence setter methode to save unparsed fieldInfluence in the given invalid measure
	 * @param secondField field which should be added to the measure
	 * @param fieldTwoParser turn the field into its s666 interpretation; empty optional if invalid
	 * @param setValidValueTwo setter methode to save parsed field in the given valid measure
	 * @param setInvalidValueTwo setter methode to save unparsed field in the given invalid measure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <FI> Type of the fieldInfluence
	 * @param <S> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <P> Type in which field should be interpreted
	 * @param <PI> Type in which fieldInfluence should be interpreted
	 * @param <CP> Type in which the combination of field and fieldInfluence should be interpreted
	 * @param <PS> Type in which field should be interpreted
	 * @param <V> Type of the created measure if valid
	 * @param <E> Type of the created measure if invalid
	 * @return the valid or invalid measure
	 */
	public static <F, FI, S, P, PI, CP, PS, V extends AbstractMeasurableType, E extends V> V makeMeasure(
			Supplier<V> newValidMeasure, Supplier<E> newInvalidMeasure,
			F field, Function<F, Optional<P>> fieldParser,
			FI fieldInfluence, Function<FI, Optional<PI>> fieldInfluenceParser,
			BiConsumer<V, CP> setValidValue, BiFunction<P, PI, CP> compoundParser,
			BiConsumer<E, String> setIndirectInvalidValue, BiConsumer<E, String> setInvalidValue,
			BiConsumer<E, String> setIndirectInvalidValueInfluence, BiConsumer<E, String> setInvalidValueInfluence,
			S secondField, Function<S, Optional<PS>> fieldTwoParser,
			BiConsumer<V, PS> setValidValueTwo, BiConsumer<E, String> setInvalidValueTwo
	) {
		var result = ResultReference.newMeasurable(newValidMeasure, newInvalidMeasure);

		Optional<P> parsedField = parseField(
				Optional.ofNullable(field),
				fieldParser,
				result
		);
		Optional<PI> parsedFieldInfluence = parseField(
				Optional.ofNullable(fieldInfluence),
				fieldInfluenceParser,
				result
		);
		Optional<PS> parsedSecond = parseField(
				Optional.ofNullable(secondField),
				fieldTwoParser,
				result
		);

		setField(
				parsedSecond, secondField,
				result,
				setValidValueTwo, setInvalidValueTwo
		);
		if (parsedField.isEmpty() || parsedFieldInfluence.isEmpty()) {
			return setInvalidCompoundField(
					parsedField, field,
					parsedFieldInfluence, fieldInfluence,
					result,
					setIndirectInvalidValue, setInvalidValue,
					setIndirectInvalidValueInfluence, setInvalidValueInfluence
			);
		} else {
			var compound = compoundParser.apply(parsedField.get(), parsedFieldInfluence.get());
			Objects.requireNonNull(compound, "FieldHandler.makeMeasure(...): The given BiFunction compoundParser returned null. When both input parameter are valid, the function must return the combined value!");
			return addToInfallibleMeasure(
					result.get(),
					compound,
					setValidValue
			);
		}
	}

	/**
	 * Interprets the given field.
	 *
	 * @param field field to interpret
	 * @param parser turn the field into its s666 interpretation; empty optional if invalid
	 * @param result saves if interpretation vas invalid
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <P> Type in which field should be interpreted
	 * @param <T> defines if the result holds a {@link AbstractMeasurableType} or {@link AbstractUnmeasurableType}
	 * @param <V> Type of the created measure if valid
	 * @param <E> Type of the created measure if invalid
	 * @return an Optional containing the interpretation of the field or null if not interpretable
	 */
	private static <F, P, T, V extends T, E extends V> Optional<P> parseField(
			Optional<F> field,
			Function<F, Optional<P>> parser,
			ResultReference<T, V, E> result
	) {
		Optional<P> interpretation = field.flatMap(parser);
		if (interpretation.isEmpty()) {
			result.requestInvalid();
		}
		return interpretation;
	}

	/**
	 * Set the field interpretation in a valid measure/unmeasure or the field in an invalid.
	 *
	 * @param parsedField interpreted field
	 * @param field field to interpret
	 * @param result provides the valid or invalid measure/unmeasure
	 * @param setValidValue setter methode to save parsed field in the given valid measure/unmeasure
	 * @param setInvalidValue setter methode to save field in the given invalid measure/unmeasure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <P> Type in which field should be interpreted
	 * @param <T> defines if the result holds a {@link AbstractMeasurableType} or {@link AbstractUnmeasurableType}
	 * @param <V> Type of the created measure if valid
	 * @param <E> Type of the created measure if invalid
	 * @return the valid or invalid measure/unmeasure
	 */
	private static <F, P, T, V extends T, E extends V> V setField(
			Optional<P> parsedField, F field,
			ResultReference<T, V, E> result,
			BiConsumer<V, P> setValidValue, BiConsumer<E, String> setInvalidValue
	) {
		parsedField.ifPresentOrElse(
				i -> setValidValue.accept(result.get(), i),
				() -> setInvalidValue.accept(result.requestInvalid(), String.valueOf(field))
		);
		return result.get();
	}

	/**
	 * Set the valid parsed field and fieldInfluence in an invalid measure/unmeasure
	 * or the unparsed field and fieldInfluence if invalid.
	 *
	 * <p>Either the field or fieldInfluence should be invalid.
	 * Otherwise, there is no need to save the interpretation in an invalid measure/unmeasure.
	 *
	 * @param parsedField interpreted field
	 * @param field field to interpret
	 * @param parsedFieldInfluence interpreted field
	 * @param fieldInfluence field to interpret
	 * @param result provides the valid or invalid measure/unmeasure
	 * @param setIndirectInvalidValue setter methode to save parsed field with unparsed fieldInfluence in the given invalid measure/unmeasure
	 * @param setInvalidValue setter methode to save unparsed field in the given invalid measure/unmeasure
	 * @param setIndirectInvalidValueInfluence setter methode to save parsed fieldInfluence with unparsed field in the given invalid measure/unmeasure
	 * @param setInvalidValueInfluence setter methode to save unparsed fieldInfluence in the given invalid measure/unmeasure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <FI> Type of the fieldInfluence
	 * @param <P> Type in which field should be interpreted
	 * @param <PI> Type in which fieldInfluence should be interpreted
	 * @param <T> defines if the result holds a {@link AbstractMeasurableType} or {@link AbstractUnmeasurableType}
	 * @param <V> Type of the created measure if valid
	 * @param <E> Type of the created measure if invalid
	 * @return the valid or invalid measure/unmeasure
	 */
	private static <F, FI, P, PI, T, V extends T, E extends V> E setInvalidCompoundField(
			Optional<P> parsedField, F field,
			Optional<PI> parsedFieldInfluence, FI fieldInfluence,
			ResultReference<T, V, E> result,
			BiConsumer<E, String> setIndirectInvalidValue, BiConsumer<E, String> setInvalidValue,
			BiConsumer<E, String> setIndirectInvalidValueInfluence, BiConsumer<E, String> setInvalidValueInfluence
	) {
		parsedField.ifPresentOrElse(
				i -> setIndirectInvalidValue.accept(result.requestInvalid(), String.valueOf(i)),
				() -> setInvalidValue.accept(result.requestInvalid(), String.valueOf(field))
		);
		parsedFieldInfluence.ifPresentOrElse(
				i -> setIndirectInvalidValueInfluence.accept(result.requestInvalid(), String.valueOf(i)),
				() -> setInvalidValueInfluence.accept(result.requestInvalid(), String.valueOf(fieldInfluence))
		);
		return result.requestInvalid();
	}

	/**
	 * Initialize the given measure, and save the given fields in it with the given setter.
	 *
	 * <p>This is the short form of the
	 * {@link #makeMeasure(Supplier, Supplier, Object, Function, BiConsumer, BiConsumer)}
	 * and the other variations with more fields, that can be used,
	 * if you already know if the measure is valid or invalid
	 *
	 * @param newMeasure constructor for the measure
	 * @param field field which should be added to the measure
	 * @param setValue setter methode to save given field in the given measure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <V> Type of the created measure
	 * @return the measure
	 */
	public static <F, V extends AbstractMeasurableType> V makeInfallibleMeasure(
			Supplier<V> newMeasure,
			F field, BiConsumer<V, F> setValue
	) {
		var measure = newMeasure.get();
		setValue.accept(measure, field);
		return measure;
	}

	/**
	 * Save the given fields in the given measure with the given setters.
	 *
	 * @param measure the measure
	 * @param field field which should be added to the measure
	 * @param setValue setter methode to save given field in the given measure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <V> Type of the created measure
	 * @return the measure
	 */
	public static <F, V extends AbstractMeasurableType> V addToInfallibleMeasure(
			V measure,
			F field, BiConsumer<V, F> setValue
	) {
		setValue.accept(measure, field);
		return measure;
	}

	/**
	 * Initialize the given measure, and save the given fields in it with the given setter.
	 *
	 * <p>This is the short form of the
	 * {@link #makeMeasure(Supplier, Supplier, Object, Function, BiConsumer, BiConsumer)}
	 * and the other variations with more fields, that can be used,
	 * if you already know if the measure is valid or invalid
	 *
	 * @param newMeasure constructor for the measure
	 * @param firstField field which should be added to the measure
	 * @param setValueOne setter methode to save given field in the given measure
	 * @param secondField field which should be added to the measure
	 * @param setValueTwo setter methode to save given field in the given measure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <S> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <V> Type of the created measure
	 * @return the measure
	 */
	public static <F, S, V extends AbstractMeasurableType> V makeInfallibleMeasure(
			Supplier<V> newMeasure,
			F firstField, BiConsumer<V, F> setValueOne,
			S secondField, BiConsumer<V, S> setValueTwo
	) {
		var measure = newMeasure.get();
		setValueOne.accept(measure, firstField);
		setValueTwo.accept(measure, secondField);
		return measure;
	}

	/**
	 * Initialize the given measure, and save the given fields in it with the given setter.
	 *
	 * <p>This is the short form of the
	 * {@link #makeMeasure(Supplier, Supplier, Object, Function, BiConsumer, BiConsumer)}
	 * and the other variations with more fields, that can be used,
	 * if you already know if the measure is valid or invalid
	 *
	 * @param newMeasure constructor for the measure
	 * @param firstField field which should be added to the measure
	 * @param setValueOne setter methode to save given field in the given measure
	 * @param secondField field which should be added to the measure
	 * @param setValueTwo setter methode to save given field in the given measure
	 * @param thirdField field which should be added to the measure
	 * @param setValueThree setter methode to save given field in the given measure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <S> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <T> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <V> Type of the created measure
	 * @return the measure
	 */
	public static <F, S, T, V extends AbstractMeasurableType> V makeInfallibleMeasure(
			Supplier<V> newMeasure,
			F firstField,
			BiConsumer<V, F> setValueOne,
			S secondField,
			BiConsumer<V, S> setValueTwo,
			T thirdField,
			BiConsumer<V, T> setValueThree
	) {
		var measure = newMeasure.get();
		setValueOne.accept(measure, firstField);
		setValueTwo.accept(measure, secondField);
		setValueThree.accept(measure, thirdField);
		return measure;
	}

	/**
	 * Initialize the given measure, and save the given fields in it with the given setter.
	 *
	 * <p>This is the short form of the
	 * {@link #makeMeasure(Supplier, Supplier, Object, Function, BiConsumer, BiConsumer)}
	 * and the other variations with more fields, that can be used,
	 * if you already know if the measure is valid or invalid
	 *
	 * @param newMeasure constructor for the measure
	 * @param firstField field which should be added to the measure
	 * @param setValueOne setter methode to save given field in the given measure
	 * @param secondField field which should be added to the measure
	 * @param setValueTwo setter methode to save given field in the given measure
	 * @param thirdField field which should be added to the measure
	 * @param setValueThree setter methode to save given field in the given measure
	 * @param fourthField field which should be added to the measure
	 * @param setValueFour setter methode to save given field in the given measure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <S> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <T> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <O> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <V> Type of the created measure
	 * @return the measure
	 */
	public static <F, S, T, O, V extends AbstractMeasurableType> V makeInfallibleMeasure(
			Supplier<V> newMeasure,
			F firstField,
			BiConsumer<V, F> setValueOne,
			S secondField,
			BiConsumer<V, S> setValueTwo,
			T thirdField,
			BiConsumer<V, T> setValueThree,
			O fourthField,
			BiConsumer<V, O> setValueFour
	) {
		var measure = newMeasure.get();
		setValueOne.accept(measure, firstField);
		setValueTwo.accept(measure, secondField);
		setValueThree.accept(measure, thirdField);
		setValueFour.accept(measure, fourthField);
		return measure;
	}

	/**
	 * Initialize the given measure, interprets the given fields with the given parser
	 * and save the given fields in the measure with the given setters.
	 *
	 * <p>The fields have to be handed over as {@link Map} with a key.
	 * The same key has to be also the input of the Function {@code setValues},
	 * which provides the setter methode that save the mapped field in the measure.
	 *
	 * <p>This is the short iterated form of the
	 * {@link #makeMeasure(Supplier, Supplier, Object, Function, BiConsumer, BiConsumer)}
	 * and the other variations with more fields, that can be used,
	 * if you already know if the measure is valid or invalid
	 *
	 * @param newMeasure constructor for the measure
	 * @param fields {@link Map} of keys and fields which should be added to a measure
	 * @param parser turn the field into its s666 interpretation
	 * @param setValues contains a function which provide to a given key the right setter methode,
	 * 		to save the field that is mapped to the key in the measure.
	 * @param <K> Type of the key, to access the value
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <P> Type in which field should be interpreted
	 * @param <V> Type of the created measure
	 * @return the measure
	 */
	public static <F, K, P, V extends AbstractMeasurableType> V makeInfallibleMeasureIterator(
			Supplier<V> newMeasure,
			Map<K, F> fields, Function<F, P> parser,
			Function<K, BiConsumer<V, P>> setValues
	) {
		var measure = newMeasure.get();
		addToInfallibleMeasureIterator(
				measure,
				fields, parser,
				setValues
		);
		return measure;
	}

	/**
	 * Interprets the given fields with the given parser
	 * and save the given fields in the given measure with the given setters.
	 *
	 * <p>The fields have to be handed over as {@link Map} with a key.
	 * The same key has to be also the input of the Function {@code setValues},
	 * which provides the setter methode that save the mapped field in the measure.
	 *
	 * @param measure the measure
	 * @param fields {@link Map} of keys and fields which should be added to a measure
	 * @param parser turn the field into its s666 interpretation
	 * @param setValues contains a function which provide to a given key the right setter methode,
	 * 		to save the field that is mapped to the key in the measure.
	 * @param <K> Type of the key, to access the value
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <P> Type in which field should be interpreted
	 * @param <V> Type of the created measure
	 */
	public static <F, K, P, V extends AbstractMeasurableType> void addToInfallibleMeasureIterator(
			V measure,
			Map<K, F> fields, Function<F, P> parser,
			Function<K, BiConsumer<V, P>> setValues
	) {
		fields.forEach(
				(key, value) -> setValues
						.apply(key)
						.accept(
								measure,
								parser.apply(value)
						)
		);
	}

	/**
	 * Initialize the given unmeasure, and save the given fields in it with the given setter.
	 *
	 * <p>This is the short form of the
	 * {@link #makeUnmeasure(Supplier, Supplier, Object, Function, BiConsumer, BiConsumer)}
	 * and the other variations with more fields, that can be used,
	 * if you already know if the measure is valid or invalid
	 *
	 * @param newUnmeasure constructor for the unmeasure
	 * @param field field which should be added to the unmeasure
	 * @param setValue setter methode to save given field in the given unmeasure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <V> Type of the created unmeasure
	 * @return the valid or invalid unmeasure
	 */
	public static <F, V extends AbstractUnmeasurableType> V makeInfallibleUnmeasure(
			Supplier<V> newUnmeasure,
			F field,
			BiConsumer<V, F> setValue
	) {
		var unmeasure = newUnmeasure.get();
		setValue.accept(unmeasure, field);
		return unmeasure;
	}

	/**
	 * Initialize the given unmeasure, and save the given fields in it with the given setter.
	 *
	 * <p>This is the short form of the
	 * {@link #makeUnmeasure(Supplier, Supplier, Object, Function, BiConsumer, BiConsumer)}
	 * and the other variations with more fields, that can be used,
	 * if you already know if the measure is valid or invalid
	 *
	 * @param newUnmeasure constructor for the unmeasure
	 * @param firstField field which should be added to the unmeasure
	 * @param setValueOne setter methode to save given field in the given unmeasure
	 * @param secondField field which should be added to the unmeasure
	 * @param setValueTwo setter methode to save given field in the given unmeasure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <S> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <V> Type of the created unmeasure
	 * @return the valid or invalid unmeasure
	 */
	public static <F, S, V extends AbstractUnmeasurableType> V makeInfallibleUnmeasure(
			Supplier<V> newUnmeasure,
			F firstField,
			BiConsumer<V, F> setValueOne,
			S secondField,
			BiConsumer<V, S> setValueTwo
	) {
		var unmeasure = newUnmeasure.get();
		setValueOne.accept(unmeasure, firstField);
		setValueTwo.accept(unmeasure, secondField);
		return unmeasure;
	}

	/**
	 * Initialize the given unmeasure, and save the given fields in it with the given setter.
	 *
	 * <p>This is the short form of the
	 * {@link #makeUnmeasure(Supplier, Supplier, Object, Function, BiConsumer, BiConsumer)}
	 * and the other variations with more fields, that can be used,
	 * if you already know if the measure is valid or invalid
	 *
	 * @param newUnmeasure constructor for the unmeasure
	 * @param firstField field which should be added to the unmeasure
	 * @param setValueOne setter methode to save given field in the given unmeasure
	 * @param secondField field which should be added to the unmeasure
	 * @param setValueTwo setter methode to save given field in the given unmeasure
	 * @param thirdField field which should be added to the unmeasure
	 * @param setValueThree setter methode to save given field in the given unmeasure
	 * @param <F> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <S> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <T> Type of the field, (most likely a BigDecimal or interpretation enum)
	 * @param <V> Type of the created unmeasure
	 * @return the valid or invalid unmeasure
	 */
	public static <F, S, T, V extends AbstractUnmeasurableType> V makeInfallibleUnmeasure(
			Supplier<V> newUnmeasure,
			F firstField,
			BiConsumer<V, F> setValueOne,
			S secondField,
			BiConsumer<V, S> setValueTwo,
			T thirdField,
			BiConsumer<V, T> setValueThree
	) {
		var unmeasure = newUnmeasure.get();
		setValueOne.accept(unmeasure, firstField);
		setValueTwo.accept(unmeasure, secondField);
		setValueThree.accept(unmeasure, thirdField);
		return unmeasure;
	}

	/**
	 * Checks if either the valid or invalid field of the given unmeasurable are set.
	 *
	 * @param unmeasurable to be extracted
	 * @param invalidUnmeasurableClass class of invalid unmeasurable
	 * @param getValidValue getter of valid value
	 * @param getInvalidValue getter of invalid value
	 * @param <UM> Unmeasurable
	 * @param <IUM> InvalidUnmeasurable
	 * @param <T> Type of valid data
	 * @return false if both are null
	 */
	public static <UM extends AbstractUnmeasurableType, IUM extends UM, T> boolean isValidOrInvalidFieldSet(
			UM unmeasurable, Class<IUM> invalidUnmeasurableClass,
			Function<UM, T> getValidValue, Function<IUM, String> getInvalidValue
	) {
		T validValue = getValidValue.apply(unmeasurable);
		if (validValue != null) {
			return true;
		} else {
			// else we have an invalid value and simply return it
			IUM invalidUnmeasurable = invalidUnmeasurableClass.cast(unmeasurable);
			String invalidValue = getInvalidValue.apply(invalidUnmeasurable);
			return invalidValue != null;
		}
	}

	/**
	 * Checks if either of the valid or invalid fields of the given is are set.
	 *
	 * @param measure to be extracted
	 * @param invalidMeasureClass class of the invalid measure
	 * @param getMagnitude getter for the measure magnitude
	 * @param getInvalidMagnitude getter of the invalid measure magnitude
	 * @param <M> Measure
	 * @param <IM> InvalidMeasure
	 * @param <T> Type of the valid magnitude
	 * @return false if both are null
	 */
	public static <M extends AbstractMeasurableType, IM extends M, T, S> boolean isValidOrInvalidFieldSet(
			M measure, Class<IM> invalidMeasureClass,
			Function<M, T> getMagnitude, Function<IM, S> getInvalidMagnitude
	) {
		T validValue = getMagnitude.apply(measure);
		if (validValue != null) {
			return true;
		} else {
			// else we have an invalid value and simply return it
			IM invalidUnmeasurable = invalidMeasureClass.cast(measure);
			S invalidValue = getInvalidMagnitude.apply(invalidUnmeasurable);
			return invalidValue != null;
		}
	}

	/**
	 * Extracts a specific data from the unmeasurable.
	 *
	 * <p>We combine extracting and parsing within this method,
	 * because the parser will only be applied to the validValue.
	 * By using the parser, we always return an expected String.
	 *
	 * @param unmeasurable to be extracted
	 * @param invalidUnmeasurableClass class of invalid unmeasurable
	 * @param getValidValue getter of valid value
	 * @param getInvalidValue getter of invalid value
	 * @param validParser parser to apply to validValue
	 * @param <UM> Unmeasurable
	 * @param <IUM> InvalidUnmeasurable
	 * @param <T> Type of valid data
	 * @return resulting data
	 */
	public static <UM extends AbstractUnmeasurableType, IUM extends UM, T> String extractFromUnmeasurable(
			UM unmeasurable, Class<IUM> invalidUnmeasurableClass,
			Function<UM, T> getValidValue, Function<IUM, String> getInvalidValue,
			Function<T, String> validParser
	) {
		T validValue = getValidValue.apply(unmeasurable);
		if (validValue != null) {
			// We only apply the parser to the valid Value, to turn it to a string
			return validParser.apply(validValue);
		} else {
			// else we have an invalid value and simply return it
			IUM invalidUnmeasurable = invalidUnmeasurableClass.cast(unmeasurable);
			String invalidValue = getInvalidValue.apply(invalidUnmeasurable);
			if (invalidValue == null) {
				throw new IllegalStateException("Unmeasurable has neither a valid nor invalid value");
			}
			return invalidValue;
		}
	}

	/**
	 * Extracts a specific data from the given measure.
	 *
	 * <p>We combine extracting and parsing within this method,
	 * because the parser will only be applied to the validValue.
	 * By using the parser, we always return an expected String.
	 *
	 * @param measure to be extracted
	 * @param invalidMeasureClass class of the invalid measure
	 * @param getMagnitude getter for the measure magnitude
	 * @param getInvalidMagnitude getter of the invalid measure magnitude
	 * @param <M> Measure
	 * @param <IM> InvalidMeasure
	 * @param <T> Type of the valid magnitude
	 * @return the resulting data
	 */
	public static <M extends AbstractMeasurableType, IM extends M, T, S> S extractFromMeasure(
			M measure, Class<IM> invalidMeasureClass,
			Function<M, T> getMagnitude, Function<IM, S> getInvalidMagnitude,
			Function<T, S> validParser
	) {
		T validValue = getMagnitude.apply(measure);
		if (validValue != null) {
			// We only apply the parser to the valid Value, to turn it to a string
			return validParser.apply(validValue);
		} else {
			// else we have an invalid value and simply return it
			IM invalidMeasure = invalidMeasureClass.cast(measure);
			S invalidValue = getInvalidMagnitude.apply(invalidMeasure);
			if (invalidValue == null) {
				throw new IllegalStateException("Measurable has neither a valid nor invalid value");
			}
			return invalidValue;
		}
	}

	/**
	 * Extracts specific data from a measure,
	 * where the data doesn't exist, when it's valid.
	 * The data will be implicitly known somewhere else.
	 *
	 * @param measure to be extracted
	 * @param invalidMeasureClass class of the invalid measure
	 * @param getInvalidValue getter of the invalid measure value
	 * @param <M> Measure
	 * @param <IM> InvalidMeasure
	 * @return either empty Optional when the searched data was valid,
	 * 		or an optional containing the invalid data
	 */
	public static <M extends AbstractMeasurableType, IM extends M> Optional<String>
	extractFromMeasureWithEmptyValidField(
			M measure, Class<IM> invalidMeasureClass,
			Function<IM, String> getInvalidValue
	) {
		// check if anything in the measure is invalid
		if (!MeasureUtil.isInvalid(measure)) {
			// when the measure is valid, we don't have anything to return
			return Optional.empty();
		} else {
			// else there could be an invalid value
			IM invalidMeasure = invalidMeasureClass.cast(measure);
			String invalidValid = getInvalidValue.apply(invalidMeasure);
			if (invalidValid != null) {
				// the searched value was invalid, so return it
				return Optional.of(invalidValid);
			} else {
				// something else made the measure invalid,
				// so we don't have anything to return
				return Optional.empty();
			}
		}
	}
}
