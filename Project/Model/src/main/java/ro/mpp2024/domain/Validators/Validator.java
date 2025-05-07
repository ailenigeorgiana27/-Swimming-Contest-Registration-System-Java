package ro.mpp2024.domain.Validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}