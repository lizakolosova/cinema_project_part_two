import Joi from 'joi';

export const loginSchema = Joi.object({
    username: Joi.string().email({ tlds: false }).required().messages({
        'string.empty': 'Username is required',
        'string.email': 'Please enter a valid email address'
    }),
    password: Joi.string().min(3).required().messages({
        'string.empty': 'Password is required',
        'string.min': 'Password must be at least 3 characters'
    })
});

export function validateLoginForm(data) {
    return loginSchema.validate(data, { abortEarly: false });
}
