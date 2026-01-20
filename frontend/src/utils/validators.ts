/**
 * Validation utilities and schemas using Yup
 */

import * as yup from 'yup';
import { VALIDATION } from './constants';

// User validation schema
export const userSchema = yup.object({
  username: yup
    .string()
    .required('Username is required')
    .min(VALIDATION.NAME_MIN_LENGTH, `Username must be at least ${VALIDATION.NAME_MIN_LENGTH} characters`)
    .max(VALIDATION.NAME_MAX_LENGTH, `Username must be at most ${VALIDATION.NAME_MAX_LENGTH} characters`),
  email: yup
    .string()
    .required('Email is required')
    .email('Invalid email format')
    .matches(VALIDATION.EMAIL_PATTERN, 'Invalid email format'),
});

// Post validation schema
export const postSchema = yup.object({
  text: yup
    .string()
    .required('Post content is required')
    .min(VALIDATION.POST_MIN_LENGTH, 'Post cannot be empty')
    .max(VALIDATION.POST_MAX_LENGTH, `Post must be at most ${VALIDATION.POST_MAX_LENGTH} characters`),
});

// Timeline filter validation schema
export const timelineFilterSchema = yup.object({
  fromDate: yup.date().nullable(),
  toDate: yup
    .date()
    .nullable()
    .when('fromDate', (fromDate, schema) => {
      return fromDate
        ? schema.min(fromDate, 'End date must be after start date')
        : schema;
    }),
});
