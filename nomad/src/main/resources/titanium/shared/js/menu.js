/* A battle for another day
var activity = Ti.Android.currentActivity;
var win = Ti.UI.currentWindow;
 
activity.onCreateOptionsMenu = function(e) {
    var menu = e.menu;
    var menuItem = menu.add({ title: "Configuration" });
    menuItem.setIcon("/images/tool.png");
    menuItem.addEventListener("click", function(e) {
      Titanium.App.fireEvent('openConfig');
   });
   
   var aboutMenu = menu.add({ title: "About" });
   aboutMenu.setIcon("/images/help.png")
   aboutMenu.addEventListener("click", function(e) {
      Titanium.App.fireEvent('openAbout');
   });
};
*/