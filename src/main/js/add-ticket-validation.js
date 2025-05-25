import Joi from 'joi';

export const addTicketSchema = Joi.object({
    price: Joi.number().positive().precision(2).required().max(20).min(4).messages({
        'number.max': 'Price must be lower than 20',
        'number.min': 'Price must be bigger than 4',
        'number.positive': 'Price must be positive',
        'number.precision': 'Price can have at most 2 decimal places',
        'any.required': 'Price is required'
    }),

    showtime: Joi.string().isoDate().required().messages({
        'string.empty': 'Show time is required',
    }),
    format: Joi.string().min(1).required().messages({
        'string.empty': 'Format is required'
    }),
    image: Joi.string().min(1).required().messages({
        'string.empty': 'Image name is required'
    }),
    availability: Joi.string().valid('AVAILABLE', 'RESERVED', 'SOLD').required().messages({
        'any.only': 'Availability must be selected'
    }),
    movie_id: Joi.string().required().messages({
        'string.empty': 'Movie must be selected'
    }),
    cinema_id: Joi.string().required().messages({
        'string.empty': 'Cinema must be selected'
    })
});

export function validateAddTicketForm(data) {
    return addTicketSchema.validate(data, { abortEarly: false });
}
