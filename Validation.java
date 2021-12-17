/**
 * @author Jiaqi Fu
 * <p>
 * This record packs the input value and validation rules
 * see@ValidationEnum
 */
public record Validation(
        Object value,
        ValidationEnum... validationEnum
) {
}
