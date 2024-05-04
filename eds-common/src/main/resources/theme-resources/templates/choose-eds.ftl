<#import "template.ftl" as layout>

<@layout.registrationLayout displayInfo=social.displayInfo; section>
    <#if section = "title">
        ${msg("loginTitle",(realm.displayName!''))}
    <#elseif section = "header">
        ${msg("loginTitleHtml",(realm.displayNameHtml!''))?no_esc}
    <#elseif section = "form">
        <div class="modal" tabindex="-1" id="NCALayerNotConnected">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">NCALayer has not been initiated</h5>
                    </div>
                    <div class="modal-body">
                        <p>Please initiate NCALayer and try again</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary"
                                data-bs-dismiss="modal">Close
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div id="NCALayerError" style="display: none;">
            <p>An error has occurred with NCALayer</p>
        </div>
        <#if realm.password>
            <form id="kc-form-login" class="${properties.kcFormClass!}" onsubmit="login.disabled = true; return true;"
                  action="${url.loginAction}" method="post">
                <div class="${properties.kcFormGroupClass!}">
                    <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    </div>

                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                        <div class="${properties.kcFormButtonsWrapperClass!}">
                            <input tabindex="4"
                                   class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}"
                                   name="chooseEds" id="chooseEds" type="button"
                                   value="Choose certificate"/>
                        </div>
                    </div>

                    <div id="signPnlSignedXMLS" style="visibility: hidden;">
                        <input type="hidden" id="certificate" name="certificate"/>
                    </div>
                    <div style="visibility: hidden;">
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}"
                               id="edsauth" name="edsauth" type="submit">
                    </div>
                </div>
            </form>

            <!-- Include your JS file -->
            <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@latest/dist/js/bootstrap.bundle.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/uuid@8.3.2/dist/umd/uuidv4.min.js"></script>
            <script src="${url.resourcesPath}/../Eds-Login/js/ncalayer-client.js" defer></script>

        </#if>
    </#if>
</@layout.registrationLayout>
