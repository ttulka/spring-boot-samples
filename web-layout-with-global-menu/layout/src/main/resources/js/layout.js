(function () {
    homeLinksInit();
})();

function homeLinksInit() {
    var homeLinks = document.getElementsByClassName('homeLink');
    if (homeLinks) {
        for (var i = 0; i < homeLinks.length; i++) {

            var path = window.location.pathname;
            if (path) {
                homeLinks[i].href = path.substring(0, path.indexOf('/', 1) + 1) || '/';
            }
        }
    }
}

// bootstrap init
$(function () {
    $('[data-toggle="tooltip"]').tooltip()
})