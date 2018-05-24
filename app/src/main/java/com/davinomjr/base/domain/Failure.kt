package com.davinomjr.base.domain

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    class ServerError(error: String) : Failure()
}

