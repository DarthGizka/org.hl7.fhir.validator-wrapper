import api.sendValidationRequest
import constants.FhirFormat
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.CliContext
import model.FileInfo
import model.ValidationOutcome
import react.*
import react.dom.div
import react.dom.h1
import uicomponents.resourceEntryField
import uicomponents.validationOutcome
import utils.assembleRequest

external interface AppState : RState {
    var validationOutcome: ValidationOutcome
    var cliContext: CliContext
}

val mainScope = MainScope()

class App : RComponent<RProps, AppState>() {
    override fun AppState.init() {
        // For testing
        validationOutcome = ValidationOutcome().setIssues(listOf())

        // Initialize CLI Context
        var context = CliContext()

        mainScope.launch {
            setState {
                // Set CLI Context
                cliContext = context
            }
        }
    }

    override fun RBuilder.render() {
        h1 {
            +"Validator GUI"
        }

        div {
            validationOutcome {
                outcome = state.validationOutcome
            }
        }

        div {
            resourceEntryField {
                onSubmit = {
                    val request = assembleRequest(state.cliContext, FileInfo().setFileName("Temp").setFileContent(it).setFileType(FhirFormat.JSON.code))
                    mainScope.launch {
                        val returnedOutcome = sendValidationRequest(request)
                        setState {
                            // Only one returned outcome in single submitted validation operation
                            println("setting state")
                            for (issue in returnedOutcome[0].getIssues()) {
                                println("${issue.getSeverity()} :: ${issue.getDetails()}")
                            }
                            validationOutcome = returnedOutcome[0]
                        }
                    }
                }
            }
        }
    }
}