package com.android.internship.presentation.components

abstract class EditTextValidator<T>(
    private val errorText: String,
) {
    abstract fun isValid(value: T): Boolean

    open operator fun invoke(value: T): String? = if (isValid(value)) null else errorText
}

class RequiredValidator(
    errorText: String,
) : EditTextValidator<String>(errorText) {
    override fun isValid(value: String): Boolean = value.trim().isNotEmpty()
}

class MinLengthValidator(
    private val min: Int,
    errorText: String,
) : EditTextValidator<String>(errorText) {
    override fun isValid(value: String): Boolean = value.length >= min
}

class EmailValidator(
    errorText: String,
) : EditTextValidator<String>(errorText) {
    private val emailPattern: String = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"

    override fun isValid(value: String): Boolean = Regex(emailPattern).containsMatchIn(value)
}

class MultiValidator(
    private val validators: List<EditTextValidator<String>>,
) : EditTextValidator<String>("") {
    private var errorText: String = ""

    override fun isValid(value: String): Boolean {
        for (validator in validators) {
            validator.invoke(value)?.let {
                errorText = it
                return false
            }
        }
        return true
    }

    override fun invoke(value: String): String? = if (isValid(value)) null else errorText
}
