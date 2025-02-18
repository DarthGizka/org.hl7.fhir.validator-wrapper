package api

import constants.IG_ENDPOINT
import constants.IG_VERSIONS_ENDPOINT
import constants.TERMINOLOGY_ENDPOINT
import constants.VALIDATION_ENDPOINT
import constants.VERSIONS_ENDPOINT
import constants.VALIDATOR_VERSION_ENDPOINT

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinext.js.asJsObject
import io.ktor.client.call.*
import model.*

import kotlinx.browser.window

suspend fun sendValidationRequest(validationRequest: ValidationRequest): ValidationResponse {
    val myMap = js("{" +
            "\"txServer\":\"dummyServer\"" +
            "}")
    println(myMap)
    kotlinx.browser.window.asDynamic().gtag("event", "validationEvent", myMap)
    return jsonClient.post(urlString = endpoint + VALIDATION_ENDPOINT) {
        contentType(ContentType.Application.Json)
        setBody(validationRequest)
    }.body()
}

suspend fun sendValidatorVersionRequest() : String {
    return jsonClient.get(urlString = endpoint + VALIDATOR_VERSION_ENDPOINT) {
        contentType(ContentType.Application.Json)
    }.body()
}


suspend fun sendIGsRequest(): IGResponse {
    return jsonClient.get(urlString = endpoint + IG_ENDPOINT).body()
}

suspend fun sendIGsRequest(partialPackageName : String): IGResponse {
    return jsonClient.get(urlString = "$endpoint$IG_ENDPOINT?name=${partialPackageName}").body()
}

suspend fun sendIGVersionsRequest(igPackageName : String) : IGResponse {
    return jsonClient.get(urlString = "$endpoint$IG_VERSIONS_ENDPOINT/${igPackageName}").body()
}

suspend fun sendVersionsRequest(): FhirVersionsResponse {
    return jsonClient.get(urlString = endpoint + VERSIONS_ENDPOINT).body()
}

suspend fun validateTxServer(url: String): TerminologyServerResponse {
    return jsonClient.post(urlString = endpoint + TERMINOLOGY_ENDPOINT) {
        contentType(ContentType.Application.Json)
        setBody(TerminologyServerRequest(url = url))
    }.body()
}

//suspend fun sendDebugMessage(message: String) {
//    val message = jsonClient.post<String>(urlString = endpoint + DEBUG_ENDPOINT) {
//        contentType(ContentType.Application.Json)
//        body = message
//    }
//}