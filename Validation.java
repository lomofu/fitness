/**
 * @author lomofu
 * @desc
 * @create 08/Dec/2021 17:53
 */
public record Validation(
        Object value,
        ValidationEnum... validationEnum
){}
