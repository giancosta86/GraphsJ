var baseURL = "https://github.com/giancosta86/GraphsJ/releases/latest"


function onMobileDevice() {
  return window.screen.availWidth < 1000
}


window.onload = function() {
  if (!onMobileDevice()) {
    setupRunWithMoonDeployButton()
    setupdownloadBinaryZipButton()
  }
}


function setupRunWithMoonDeployButton() {
  getLatestMoonDescriptor(
    baseURL,

    "App.moondeploy",

    function(descriptorURL) {
      var runButton = document.getElementById("runWithMoonDeployButton")
      runButton.href = descriptorURL
      runButton.style.display = "inline-block"
    },

    function(statusCode) {
      alert(`Error while retrieving MoonDeploy descriptor. Status code: ${statusCode}`)
    }
  )
}


function setupdownloadBinaryZipButton() {
  getLatestReleaseArtifact(
    baseURL,

    "GraphsJ",

    ".zip",

    function(zipURL) {
      var downloadBinaryZipButton = document.getElementById("downloadBinaryZipButton")
      downloadBinaryZipButton.href = zipURL
      downloadBinaryZipButton.style.display = "inline-block"
    },

    function(statusCode) {
      alert(`Error while retrieving the binary zip artifact. Status code: ${statusCode}`)
    }
  )
}
