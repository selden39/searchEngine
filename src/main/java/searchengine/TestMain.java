package searchengine;

import searchengine.services.lemmatization.Lemmatizer;

public class TestMain {
    public static void main(String[] args) throws Exception {

        String html = getHtml();
        Lemmatizer lemmatizer = new Lemmatizer();
        lemmatizer.getLemmasFromText(html).forEach((lemma, count) -> System.out.println(lemma + " - " + count));

    }

    public static String getHtmlShort(){
        String html = "<!doctype html>\n" +
                " <html>\n" +
                "  <head>\n" +
                "   <title>Оплата</title>\n" +
                "   <meta name=\"description\" content=\"Продажа по доступным ценам. PlayBack.ru - Интернет-Магазин - Большой выбор смартфонов, планшетов, носимой электроники по низким ценам, отличный сервис, гарантии производителя\">\n" +
                "   <meta name=\"keywords\" content=\"купить, цена, описание, интернет-магазин, интернет, магазин, продажа, смартфоны\">\n" +
                "   <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "   <meta http-equiv=\"Last-Modified\" content=\"Fri, 23 Sep 2022 11:15:01 GMT\">\n" +
                "   <link rel=\"shortcut icon\" href=\"/favicon.ico\">\n" +
                "   <link rel=\"apple-touch-icon\" href=\"/logo_apple.png\">\n" +
                "   <link rel=\"StyleSheet\" href=\"/include_new/styles.css\" type=\"text/css\" media=\"all\">\n" +
                "   <link rel=\"stylesheet\" href=\"/include_new/jquery-ui.css\">\n" +
                "   <script src=\"https://code.jquery.com/jquery-1.8.3.js\"></script>\n" +
                "   <script src=\"https://code.jquery.com/ui/1.10.0/jquery-ui.js\"></script>\n" +
                "   <script src=\"/jscripts/jquery.inputmask.js\" type=\"text/javascript\"></script>\n" +
                "   <script src=\"/jscripts/jquery.inputmask.extensions.js\" type=\"text/javascript\"></script>\n" +
                " \t<td colspan=\"3\" style=\"color: #2556A3; font:17px Roboto-Regular,Helvetica,sans-serif; text-align: center; height: 35px;vertical-align: middle;padding-bottom:10px;\">\n" +
                " \t\t<b>Уважаемые покупатели! График работы нашего магазина в новогодние праздники: <br>31 декабря - с 11 до 15, 1 января - выходной, со 2 по 8 января - с 11 до 18 .</b>\n" +
                " \t</td>\n" ;
        return html;
    }

    public static String getHtml(){
        String html = "<!doctype html>\n" +
                " <html>\n" +
                "  <head>\n" +
                "   <title>Оплата</title>\n" +
                "   <meta name=\"description\" content=\"Продажа по доступным ценам. PlayBack.ru - Интернет-Магазин - Большой выбор смартфонов, планшетов, носимой электроники по низким ценам, отличный сервис, гарантии производителя\">\n" +
                "   <meta name=\"keywords\" content=\"купить, цена, описание, интернет-магазин, интернет, магазин, продажа, смартфоны\">\n" +
                "   <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "   <meta http-equiv=\"Last-Modified\" content=\"Fri, 23 Sep 2022 11:15:01 GMT\">\n" +
                "   <link rel=\"shortcut icon\" href=\"/favicon.ico\">\n" +
                "   <link rel=\"apple-touch-icon\" href=\"/logo_apple.png\">\n" +
                "   <link rel=\"StyleSheet\" href=\"/include_new/styles.css\" type=\"text/css\" media=\"all\">\n" +
                "   <link rel=\"stylesheet\" href=\"/include_new/jquery-ui.css\">\n" +
                "   <script src=\"https://code.jquery.com/jquery-1.8.3.js\"></script>\n" +
                "   <script src=\"https://code.jquery.com/ui/1.10.0/jquery-ui.js\"></script>\n" +
                "   <script src=\"/jscripts/jquery.inputmask.js\" type=\"text/javascript\"></script>\n" +
                "   <script src=\"/jscripts/jquery.inputmask.extensions.js\" type=\"text/javascript\"></script>\n" +
                "   <script src=\"/jscripts/jquery.inputmask.numeric.extensions.js\" type=\"text/javascript\"></script>\n" +
                "   <link rel=\"stylesheet\" type=\"text/css\" href=\"/fancybox/jquery.fancybox-1.3.4.css\" media=\"screen\">\n" +
                "   <script type=\"text/javascript\" src=\"/fancybox/jquery.mousewheel-3.0.4.pack.js\"></script>\n" +
                "   <script type=\"text/javascript\" src=\"/fancybox/jquery.fancybox-1.3.4.js\"></script>\n" +
                "   <script type=\"text/javascript\" src=\"/include_new/playback.js\"></script>\n" +
                "   <script>\n" +
                "   $( function() {\n" +
                "     $( \"#accordion\" ).accordion({\n" +
                "       heightStyle: \"content\",\n" +
                " \t  collapsible: true,\n" +
                " \t  active : false,\n" +
                " \t  activate: function( event, ui ) {\n" +
                "          if ($(ui.newHeader).offset() != null) {\n" +
                "         ui.newHeader,\n" +
                "         $(\"html, body\").animate({scrollTop: ($(ui.newHeader).offset().top)}, 500);\n" +
                "       }\n" +
                "     }\n" +
                "     });\n" +
                " \t} );\n" +
                " \t$( function() {\n" +
                "     var icons = {\n" +
                "       header: \"ui-icon-circle-arrow-e\",\n" +
                "       activeHeader: \"ui-icon-circle-arrow-s\"\n" +
                "     };\n" +
                "     $( \"#accordion\" ).accordion({\n" +
                "       icons: icons\n" +
                "     });\n" +
                "     $( \"#toggle\" ).button().on( \"click\", function() {\n" +
                "       if ( $( \"#accordion\" ).accordion( \"option\", \"icons\" ) ) {\n" +
                "         $( \"#accordion\" ).accordion( \"option\", \"icons\", null );\n" +
                "       } else {\n" +
                "         $( \"#accordion\" ).accordion( \"option\", \"icons\", icons );\n" +
                "       }\n" +
                "     });\n" +
                "   } );\n" +
                "   </script>\n" +
                "   <script type=\"text/javascript\">\n" +
                "   $(function() {\n" +
                "  \n" +
                " $(window).scroll(function() {\n" +
                "  \n" +
                " if($(this).scrollTop() != 0) {\n" +
                "  \n" +
                " $('#toTop').fadeIn();\n" +
                "  \n" +
                " } else {\n" +
                "  \n" +
                " $('#toTop').fadeOut();\n" +
                "  \n" +
                " }\n" +
                "  \n" +
                " });\n" +
                "  \n" +
                " $('#toTop').click(function() {\n" +
                "  \n" +
                " $('body,html').animate({scrollTop:0},800);\n" +
                "  \n" +
                " });\n" +
                "  \n" +
                " });\n" +
                "  \n" +
                " </script>\n" +
                "  </head>\n" +
                "  <body class=\"body_undertop\" topmargin=\"0\" leftmargin=\"0\" bottommargin=\"0\" rightmargin=\"0\" align=\"center\">\n" +
                "   <table class=\"table1\" style=\"box-shadow:0px 0px 32px #595959; margin:5px auto; \" bgcolor=\"#ffffff\" width=\"1024\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">\n" +
                "    <tbody>\n" +
                "     <tr>\n" +
                "      <td colspan=\"3\" width=\"1024\">\n" +
                "       <table width=\"100%\" border=\"0\" height=\"110px\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin-top: 0px; margin-bottom: 0px;\">\n" +
                "        <tbody>\n" +
                "         <tr>\n" +
                "          <td width=\"365px\" rowspan=\"2\" align=\"left\">\n" +
                "           <table width=\"250px\" align=\"left\">\n" +
                "            <tbody>\n" +
                "             <tr>\n" +
                "              <td width=\"60px\" height=\"60px\"><img onclick=\"document.location='http://www.playback.ru';return false\" src=\"/img_new/lolo.png\" class=\"logotip\" alt=\"Playback.ru - фотоаппараты, видеокамеры и аксессуары к ним\" title=\"Playback.ru - фотоаппараты, видеокамеры и аксессуары к ним\"></td>\n" +
                "              <td valign=\"center\" align=\"left\"><a class=\"tele_span\" href=\"/\"><span class=\"tele_span_playback\">PlayBack.ru</span></a><br><span style=\"cursor: pointer;\" onclick=\"document.location='/waytoplayback.html';return false\" class=\"getcallback2\">5 минут от метро ВДНХ</span></td>\n" +
                "             </tr>\n" +
                "            </tbody>\n" +
                "           </table></td>\n" +
                "          <td width=\"3px\" rowspan=\"2\" align=\"center\">&nbsp;</td>\n" +
                "          <td width=\"290px\" rowspan=\"2\">\n" +
                "           <table width=\"215px\" align=\"center\">\n" +
                "            <tbody>\n" +
                "             <tr>\n" +
                "              <td valign=\"center\" align=\"center\"><span class=\"tele_span\"><nobr><a href=\"tel:+74951437771\">8(495)143-77-71</a></nobr></span><span class=\"grrafik\"><nobr><br>\n" +
                "                 пн-пт: c 11 до 20<br>\n" +
                "                 сб-вс: с 11 до 18</nobr></span></td>\n" +
                "             </tr>\n" +
                "            </tbody>\n" +
                "           </table></td>\n" +
                "          <td width=\"3px\" align=\"center\" rowspan=\"2\">&nbsp;</td>\n" +
                "          <td width=\"185px\">\n" +
                "           <table width=\"175px\" align=\"center\">\n" +
                "            <tbody>\n" +
                "             <tr>\n" +
                "              <td valign=\"center\" align=\"center\"><span class=\"blocknamezpom\" style=\"cursor: pointer;\" onclick=\"document.location='/tell_about_the_problem.html';return false\">Возникла проблема?<br>\n" +
                "                Напишите нам!</span></td>\n" +
                "             </tr>\n" +
                "            </tbody>\n" +
                "           </table><span class=\"tele_span\"></span></td>\n" +
                "          <td width=\"3px\" align=\"center\">&nbsp;</td>\n" +
                "          <td width=\"179px\">\n" +
                "           <table width=\"175px\" align=\"center\">\n" +
                "            <tbody>\n" +
                "             <tr>\n" +
                "              <td width=\"53px\" height=\"50px\" rowspan=\"2\" align=\"left\"><a href=\"/basket.html\"><img src=\"/img_new/basket.png\" width=\"49px\" border=\"0\"></a></td>\n" +
                "              <td valign=\"bottom\" align=\"left\" height=\"25px\"><a class=\"tele_span2\" href=\"/basket.html\">Корзина</a><br><span class=\"take_me_call\"></span></td>\n" +
                "             </tr>\n" +
                "             <tr>\n" +
                "              <td height=\"10px\" align=\"right\" valign=\"top\"><span class=\"basket_inc_label\" id=\"sosotoyaniekorziny\">пуста</span></td>\n" +
                "             </tr>\n" +
                "            </tbody>\n" +
                "           </table></td>\n" +
                "         </tr>\n" +
                "         <tr>\n" +
                "          <td colspan=\"3\" style=\"text-align: right;\">\n" +
                "           <form action=\"/search.php\" method=\"get\" class=\"izkat\">\n" +
                "            <input type=\"search\" name=\"search_string\" placeholder=\"поиск\" class=\"ssstring\"> <input type=\"submit\" name=\"\" value=\"Искать\" class=\"iskat\">\n" +
                "           </form></td>\n" +
                "         </tr>\n" +
                "        </tbody>\n" +
                "       </table></td><!--<tr> \n" +
                " \t<td colspan=\"3\" style=\"color: #2556A3; font:17px Roboto-Regular,Helvetica,sans-serif; text-align: center; height: 35px;vertical-align: middle;padding-bottom:10px;\">\n" +
                " \t\t<b>Уважаемые покупатели! График работы нашего магазина в новогодние праздники: <br>31 декабря - с 11 до 15, 1 января - выходной, со 2 по 8 января - с 11 до 18 .</b>\n" +
                " \t</td>\n" +
                "   </tr>--->\n" +
                "     </tr>\n" +
                "     <tr>\n" +
                "      <td colspan=\"3\" style=\"text-align: center;\">\n" +
                "       <nav>\n" +
                "        <ul class=\"topmenu\">\n" +
                "         <li><a href=\"\" class=\"active\" onclick=\"return false;\"><img src=\"/img/imglist.png\" height=\"9px\"> Каталог<span class=\"fa fa-angle-down\"></span></a>\n" +
                "          <ul class=\"submenu\">\n" +
                "           <li><a href=\"/catalog/1652.html\">Чехлы для смартфонов Infinix</a></li>\n" +
                "           <li><a href=\"/catalog/1511.html\">Смартфоны</a></li>\n" +
                "           <li><a href=\"/catalog/1300.html\">Чехлы для смартфонов Xiaomi</a></li>\n" +
                "           <li><a href=\"/catalog/1302.html\">Защитные стекла для смартфонов Xiaomi</a></li>\n" +
                "           <li><a href=\"/catalog/1308.html\">Чехлы для смартфонов Samsung</a></li>\n" +
                "           <li><a href=\"/catalog/1307.html\">Защитные стекла для смартфонов Samsung</a></li>\n" +
                "           <li><a href=\"/catalog/1315.html\">Зарядные устройства и кабели</a></li>\n" +
                "           <li><a href=\"/catalog/1329.html\">Держатели для смартфонов</a></li>\n" +
                "           <li><a href=\"/catalog/665.html\">Автодержатели</a></li>\n" +
                "           <li><a href=\"/catalog/1304.html\">Носимая электроника</a></li>\n" +
                "           <li><a href=\"/catalog/1305.html\">Наушники и колонки</a></li>\n" +
                "           <li><a href=\"/catalog/805.html\">Запчасти для телефонов</a></li>\n" +
                "           <li><a href=\"/catalog/1311.html\">Чехлы для планшетов</a></li>\n" +
                "           <li><a href=\"/catalog/1317.html\">Аксессуары для фото-видео</a></li>\n" +
                "           <li><a href=\"/catalog/1318.html\">Чехлы для смартфонов Apple</a></li>\n" +
                "           <li><a href=\"/catalog/1429.html\">USB Флеш-накопители</a></li>\n" +
                "           <li><a href=\"/catalog/1507.html\">Защитные стекла для смартфонов Realme</a></li>\n" +
                "           <li><a href=\"/catalog/1508.html\">Чехлы для смартфонов Realme</a></li>\n" +
                "           <li><a href=\"/catalog/18.html\">Карты памяти</a></li>\n" +
                "           <li><a href=\"/catalog/1303.html\">Защитные стекла для планшетов</a></li>\n" +
                "           <li><a href=\"/catalog/1312.html\">Защитные стекла для смартфонов</a></li>\n" +
                "           <li><a href=\"/catalog/1622.html\">Защитные стекла для смартфонов Apple</a></li>\n" +
                "           <li><a href=\"/catalog/1626.html\">Чехлы для смартфонов Vivo</a></li>\n" +
                "           <li><a href=\"/catalog/1636.html\">Чехлы для смартфонов Tecno</a></li>\n" +
                "          </ul></li>\n" +
                "         <li><a href=\"/dostavka.html\">Доставка</a></li>\n" +
                "         <li><a href=\"/pickup.html\">Самовывоз</a></li>\n" +
                "         <li><a href=\"/payment.html\">Оплата</a></li>\n" +
                "         <li><a href=\"/warranty.html\">Гарантия и обмен</a></li>\n" +
                "         <li><a href=\"/contacts.html\">Контакты</a></li>\n" +
                "        </ul>\n" +
                "       </nav></td>\n" +
                "     </tr>\n" +
                "     <tr>\n" +
                "      <td colspan=\"3\" valign=\"top\">\n" +
                "       <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "        <tbody>\n" +
                "         <tr>\n" +
                "          <!----<td class=\"menu_full_cell\" width=\"253\">---->\n" +
                "          <td colspan=\"2\" class=\"item_full_cell\">\n" +
                "           <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "            <tbody>\n" +
                "             <tr>\n" +
                "              <td colspan=\"2\" style=\"text-align:left; padding-top:15px;\"><a class=\"button15\" href=\"/\">◄ На главную страницу</a></td>\n" +
                "             </tr>\n" +
                "             <tr>\n" +
                "              <td colspan=\"2\"></td>\n" +
                "             </tr>\n" +
                "             <tr>\n" +
                "              <td class=\"text_cell\" id=\"contentcell\"><span class=\"vip_info2\"><br><span class=\"blocknamez\">Оплата наличными</span>\n" +
                "                <p>Оплатить товар наличными можно только при получении заказа в Москве – курьеру по факту доставки либо в пункте выдачи нашего магазина. При оплате наличными клиент получает товарный и кассовый чеки.</p><span class=\"blocknamez\">Оплата картой (только при самовывозе в Москве, +7%)</span>\n" +
                "                <p>Оплатить товар картой можно только при получении заказа в Москве в пункте выдачи нашего магазина. При оплате картой клиент получает товарный и кассовый чеки. Комиссия при оплате составляет 7%.</p><span class=\"blocknamez\">Перевод на карту Сбербанка (ТОЛЬКО ДЛЯ ПОДМОСКОВЬЯ И РЕГИОНОВ)</span>\n" +
                "                <p>Оплата на карточку Сбербанка удобна не только владельцам карт данного банка, но и всем иногородним клиентам магазина. Обилие отделений банка и терминалов делают этот канал денежных переводов массовым. К тому же, при оплате через личный кабинет или с мобильного приложения Сбербанка зачисление происходит средств мгновенно. При переводе, в примечании платежа необходимо необходимо прислать фото или скан перевода с указанием номера заказа на наш контактный е-мейл <a href=\"mailto:zakaz@playback.ru\">zakaz@playback.ru</a>. Номер нашей карты вам сообщит менеджер после обработки заказа. При оплате на банковскую карту клиент получает товарый и кассовый чеки. Отгрузка осуществляется после поступления платежа на расчетный счет.</p></span></td>\n" +
                "             </tr>\n" +
                "            </tbody>\n" +
                "           </table></td>\n" +
                "         </tr>\n" +
                "        </tbody>\n" +
                "       </table></td>\n" +
                "     </tr>\n" +
                "     <tr>\n" +
                "      <td colspan=\"3\" align=\"center\">\n" +
                "       <div class=\"footer\">\n" +
                "        <div class=\"footer_block\">\n" +
                "         <span class=\"footer_h1\">Информация</span>\n" +
                "         <br><a href=\"/\">Наши спецпредложения</a>\n" +
                "         <br><a href=\"/dostavka.html\">Доставка</a>\n" +
                "         <br><a href=\"/payment.html\">Оплата</a>\n" +
                "         <br><a href=\"/warranty.html\">Гарантия</a>\n" +
                "         <br><a href=\"/contacts.html\">Контакты</a>\n" +
                "         <br><a href=\"/privacy_policy.html\">Положение о конфиденциальности и защите персональных данных</a>\n" +
                "        </div>\n" +
                "        <div class=\"footer_block_cont\">\n" +
                "         <span class=\"footer_tel\">+7(495)143-77-71</span>\n" +
                "         <br>\n" +
                "         <br><a class=\"footer_email\" href=\"http://vk.com/playback_ru\" target=\"_blank\"><img src=\"/img/VK.png\" title=\"Наша страница Вконтакте\"></a> &nbsp;&nbsp; \n" +
                "         <br>\n" +
                "         <br>\n" +
                "        </div>\n" +
                "        <div class=\"footer_block_cont\" style=\"width:260px;\">\n" +
                "         <span class=\"footer_h1\">График работы:</span>\n" +
                "         <br>\n" +
                "          пн-пт: c 11-00 до 20-00 \n" +
                "         <br>\n" +
                "          сб-вс: с 11-00 до 18-00 \n" +
                "         <br>\n" +
                "         <br><span class=\"footer_h1\">Наш адрес:</span>\n" +
                "         <br>\n" +
                "          Москва, Звездный бульвар, 10, \n" +
                "         <br>\n" +
                "          строение 1, 2 этаж, офис 10.\n" +
                "        </div>\n" +
                "        <div class=\"footer_block\">\n" +
                "        </div>\n" +
                "        <div class=\"footer_block\">\n" +
                "         <script type=\"text/javascript\" src=\"//vk.com/js/api/openapi.js?105\"></script>\n" +
                "         <div id=\"vk_groups\"></div>\n" +
                "         <script type=\"text/javascript\">\n" +
                " VK.Widgets.Group(\"vk_groups\", {mode: 0, width: \"260\", height: \"210\", color1: 'FFFFFF', color2: '0C5696', color3: '0064BA'}, 48023501);\n" +
                " </script>\n" +
                "        </div>\n" +
                "       </div>\n" +
                "       <div style=\"width: 1024px; font-family: Roboto-Regular,Helvetica,sans-serif; text-align: right; font-size: 12px; text-align: left; padding-left: 10px; color: #595959; background: url(/img/footer-fon.png) repeat;\">\n" +
                "        2005-2025 ©Интернет магазин PlayBack.ru\n" +
                "       </div><!-- Yandex.Metrika counter -->\n" +
                "       <script type=\"text/javascript\">\n" +
                "    (function(m,e,t,r,i,k,a){m[i]=m[i]||function(){(m[i].a=m[i].a||[]).push(arguments)};\n" +
                "    m[i].l=1*new Date();k=e.createElement(t),a=e.getElementsByTagName(t)[0],k.async=1,k.src=r,a.parentNode.insertBefore(k,a)})\n" +
                "    (window, document, \"script\", \"https://mc.yandex.ru/metrika/tag.js\", \"ym\");\n" +
                " \n" +
                "    ym(232370, \"init\", {\n" +
                "         clickmap:true,\n" +
                "         trackLinks:true,\n" +
                "         accurateTrackBounce:true,\n" +
                "         webvisor:true\n" +
                "    });\n" +
                " </script>\n" +
                "       <noscript>\n" +
                "        <div>\n" +
                "         <img src=\"https://mc.yandex.ru/watch/232370\" style=\"position:absolute; left:-9999px;\" alt=\"\">\n" +
                "        </div>\n" +
                "       </noscript><!-- /Yandex.Metrika counter --> <!-- BEGIN JIVOSITE CODE {literal} -->\n" +
                "       <script type=\"text/javascript\">\n" +
                " (function(){ var widget_id = '8LKJc6dMce';var d=document;var w=window;function l(){\n" +
                "   var s = document.createElement('script'); s.type = 'text/javascript'; s.async = true;\n" +
                "   s.src = '//code.jivosite.com/script/widget/'+widget_id\n" +
                "     ; var ss = document.getElementsByTagName('script')[0]; ss.parentNode.insertBefore(s, ss);}\n" +
                "   if(d.readyState=='complete'){l();}else{if(w.attachEvent){w.attachEvent('onload',l);}\n" +
                "   else{w.addEventListener('load',l,false);}}})();\n" +
                " </script><!-- {/literal} END JIVOSITE CODE --></td>\n" +
                "     </tr>\n" +
                "    </tbody>\n" +
                "   </table><a href=\"#\" class=\"scrollup\">Наверх</a>\n" +
                "  </body>\n" +
                " </html>";
        return html;
    }
}
