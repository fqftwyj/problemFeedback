layui.define(['element','jquery','index'], function(exports) {
	var element = layui.element;
	var $ = layui.$;
	var menu = {
		init: function(){
			menu.bindEvent();
			menu.contEvent();
		},
		bindEvent: function() {
			// 菜单侧边按钮
			$(".yw-icon-wrap").click(function() {
				if($(".yw-side-menu").width() > 60) {
					$(".yw-menu-home .yw-icon-home,.yw-menu-home span").hide();
					$(".yw-icon-menu").css("position", "absolute");
					$(".yw-icon-menu").css("left", "20px");
				} else {
					$(".yw-menu-home .yw-icon-home,.yw-menu-home span").css("display", "inline-block");
					$(".yw-icon-menu").css("left", "190px");
				}
			})
			$(".JS-nav").click(function(){
				if($(".yw-side-menu").width() < 61){
					$(".yw-menu-home .yw-icon-home,.yw-menu-home span").css("display", "inline-block");
					$(".yw-icon-menu").css("left", "190px");
				}
			})
		},
		contEvent: function(){
			if($(".JS-tab-title li").hasClass("layui-this")){
				$(".JS-tab-title li").addClass("JS-li")
			}
			element.on('nav(yw-nav)',function(elem){
				var dataId = $(this).parent('li.JS-nav-item').attr("data-id");
				var oWord = elem.context.innerText;
				setTimeout(function(){
					var oText = $(".yw-menu-home").children("span").text(oWord);
				},10);
				var oTitle = $(this).context.innerText;
				if(dataId==0){
					removeNoCurrentTab()
					function removeNoCurrentTab() {
						var tabsUl = $('#LAY_app_tabsheader');
						layui.each(tabsUl.children(), function(key, el) {
							$(el).remove()
						})
					}
					var dataSrc = $(this).parent('li.JS-nav-item').attr("iframeSrc");
					var Src =$("#JS-href").attr("src",dataSrc);
				}else if(dataId==1){
					
				}
				
			})
			
		}
	}
	exports("menu", menu)

});