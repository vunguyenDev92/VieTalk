package com.android.internship.presentation.components

import android.content.Context
import com.android.internship.R

class Validator(private val context: Context) {

    // Validator for passwords with minimum length
    fun passwordValidator(password: String): String? = MultiValidator(
        listOf(
            RequiredValidator(errorText = context.getString(R.string.password_is_required)),
            MinLengthValidator(
                min = 8,
                errorText = context.getString(R.string.invalid_password),
            ),
            PasswordValidator(errorText = context.getString(R.string.password_must_contain_at_least_one_letter_and_one_number)),
        ),
    ).invoke(password)

    // Validator for email addresses
    fun emailValidator(email: String): String? = MultiValidator(
        listOf(
            RequiredValidator(errorText = context.getString(R.string.email_is_required)),
            EmailValidator(errorText = context.getString(R.string.invalid_email)),
        ),
    ).invoke(email)
}
