/**
 * Represents the NCALayer class used for interacting with NCA layer.
 */
class NCALayer {
    constructor() {
        this.webSocket = null;
        this.heartbeatMsg = '--heartbeat--';
        this.heartbeatInterval = null;
        this.callback = null;
    }

    /**
     * Sends a ping to the server to check if the connection is still alive.
     *
     * @method pingLayer
     * @return {void}
     */
    pingLayer() {
        try {
            this.webSocket.send(this.heartbeatMsg);
        } catch (error) {
            clearInterval(this.heartbeatInterval);
            this.heartbeatInterval = null;
            this.webSocket.close();
        }
    }

    /**
     * Opens the "Not Connected" modal for the NCA layer.
     * @function openNCALayerNotConnectedModal
     * @returns {void}
     */
    openNCALayerNotConnectedModal() {
        $('#NCALayerNotConnected').modal('show')
    }

    /**
     * Initialize the NCALayer with a callback method.
     * @param {string} callbackM - The name of the callback method.
     * @return {void}
     */
    initNCALayer(callbackM) {
        console.log('Init NCALayer with callBack method ' + callbackM);
        this.webSocket = new WebSocket('wss://127.0.0.1:13579/');

        this.webSocket.onopen = () => {
            if (this.heartbeatInterval === null) {
                this.heartbeatInterval = setInterval(() => this.pingLayer(), 1000);
            }

            if (callbackM) {
                this[callbackM]();
            }
        };

        this.webSocket.onclose = event => {
            if (!event.wasClean) {
                this.openNCALayerNotConnectedModal();
            }
        };

        this.webSocket.onmessage = event => {
            let result;
            let rw;

            if (event.data === this.heartbeatMsg) {
                return;
            }

            result = JSON.parse(event.data);

            rw = {
                result: result.result,
                secondResult: result.secondResult,
                errorCode: result.errorCode,
                code: result.code,
                responseObject: result.responseObject,
                message: result.message,
                getResult: function () {
                    return this.result;
                },
                getSecondResult: function () {
                    return this.secondResult;
                },
                getErrorCode: function () {
                    return this.errorCode;
                },
                getMessage: function () {
                    return this.message;
                },
                getResponseObject: function () {
                    return this.responseObject;
                },
                getCode: function () {
                    return this.code;
                }
            };
            if (this.callback) {
                this[this.callback](rw);
            }
        };
    }

    /**
     * A method to fetch new data using the provided method, arguments, and callback.
     *
     * @param {string} method - The method to be executed for fetching data.
     * @param {Array} args - The arguments to be passed to the method.
     * @param {function} callbackM - The callback function to be invoked after data is fetched.
     *
     * @return {void}
     */
    getDataNew(method, args, callbackM) {
        const methodVariable = {
            'module': 'kz.gov.pki.knca.commonUtils',
            'method': method,
            'args': args
        };

        if (callbackM) this.callback = callbackM;
        this.webSocket.send(JSON.stringify(methodVariable));
    }

    /**
     * Displays the 'NCALayerError' element by setting its style display property to an empty string.
     *
     * @return {void}
     */
    openNcaLayerError() {
        document.getElementById('NCALayerError').style.display = '';
    }

    /**
     * Signs the XML data and updates the certificate value.
     *
     * @param {Object} result - The result object containing the code and response.
     * @return {void}
     */
    signXmlNewBack(result) {
        let signedData;

        if (result['code'] === "500") {
            if (result['message'] != null && result['message'] !== 'action.canceled') {
                this.openNcaLayerError();
            }
        } else if (result['code'] === "200") {
            signedData = result['responseObject'];
            document.getElementById('certificate').value = signedData;
            this.webSocket.close();
            $("#edsauth").click();
        }
    }

    /**
     * Executes the selectNCAStore method.
     *
     * @return {void}
     */
    selectNCAStore() {
        setTimeout(() => {
            this.getActiveTokens('getActiveTokensBack');
        }, 500);
    }

    /**
     * Sign XML using the provided data and invoke the specified callback function.
     *
     * @param {function} callbackM - The callback function to be invoked after signing the XML.
     *
     * @return {void} - This method does not return any value.
     */
    signXmlNewCall(callbackM) {
        const currentTimeMillis = new Date().getTime();
        const data = '<?xml version="1.0" encoding="UTF-8"?><login><timeTicket>' + currentTimeMillis + '</timeTicket><sessionid>' + uuidv4() + '</sessionid></login>';
        let args = [];

        if (data) {
            args = ['PKCS12', 'AUTHENTICATION', data, "", ""];
            this.getDataNew('signXml', args, callbackM);
        } else {
            this.openNcaLayerError();
        }
    }

    /**
     * Sends a request to get active tokens.
     *
     * @param {function} callBack - The callback function that will be called when the response is received.
     * @return {void}
     */
    getActiveTokens(callBack) {
        var activeTokens = {
            "module": "kz.gov.pki.knca.commonUtils",
            "method": "getActiveTokens"
        };
        this.callback = callBack;
        this.webSocket.send(JSON.stringify(activeTokens));
    }

    /**
     * Activates tokens based on the result code.
     *
     * @param {Object} result - The result object containing the code.
     */
    getActiveTokensBack(result) {
        if (result['code'] === "500") {
            this.openNcaLayerError();
        } else if (result['code'] === "200") {
            this.doSignXML();
        }
    }

    /**
     * Selects a sign type based on the current state of the WebSocket connection.
     * If the connection is not open or is in the process of closing, it initializes the NCA layer with the 'selectNCAStore' command.
     * Otherwise, it calls the 'selectNCAStore' method directly.
     *
     * @return {void}
     */
    selectSignType() {
        /**
         * CONNECTING   0   The connection is not yet open.
         * OPEN         1   The connection is open and ready to communicate.
         * CLOSING      2   The connection is in the process of closing.
         * CLOSED       3   The connection is closed or couldn't be opened.
         */
        if (this.webSocket === null || this.webSocket.readyState === 3 || this.webSocket.readyState === 2) {
            this.initNCALayer('selectNCAStore');
        } else {
            this.selectNCAStore();
        }
    }

    /**
     * Signs an XML using the 'signXmlNewBack' method.
     *
     * @returns {boolean} - Indicates whether the XML signing was successful or not.
     */
    doSignXML() {
        this.signXmlNewCall('signXmlNewBack');
        return false;
    }
};

// Prepare to initialize and use the class based on the UI interactions
const ncaHelper = new NCALayer();
document.getElementById('chooseEds').addEventListener('click', function() {
    ncaHelper.selectSignType();
});