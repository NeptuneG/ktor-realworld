package com.neptuneg.infrastructure.exceptions

class ValidationException(message: String?) : Exception(message)
class UnexpectedException(message: String?) : Exception(message)
class UnprocessableEntityException(message: String?) : Exception(message)
class NotFoundException(message: String?) : Exception(message)
class ConflictException(message: String?) : Exception(message)
class ForbiddenException(message: String?) : Exception(message)
