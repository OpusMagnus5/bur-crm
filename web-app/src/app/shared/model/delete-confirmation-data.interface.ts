export interface DeleteConfirmationDataInterface {
  codeForTranslation: string,
  callbackArgument: string,
  removeCallback: (id: string) => void
}
