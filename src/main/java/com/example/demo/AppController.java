package com.example.demo;

import com.example.demo.json.BotWebhook;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Spring Boot Hello案例
 * <p>
 * Created by bysocket on 26/09/2017.
 */
@RestController
public class AppController {

    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public String sayHello() {
        return "Hello";
    }

    @RequestMapping(value = "/app", method = RequestMethod.POST)
    public String postBot(@RequestBody BotWebhook webhook) throws IOException {
        System.out.println(webhook);
        String response = "{\"fulfillmentText\": \"\",\n" +
                "     \"source\": \"dad jokes\"\n" +
                "    }";
        if (webhook != null && webhook.getQueryResult() != null && webhook.getQueryResult().getParameters() != null) {
            String subject = webhook.getQueryResult().getParameters().getSubject();
            if ((subject != null) && !(subject.equals(""))) {
                response = "{\"fulfillmentText\": \"" + process(subject) + "\",\n" +
                        "     \"source\": \"dad jokes\"\n" +
                        "    }";
            }
        };
        return response;
    }

    private String doQuery(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient();
        System.out.println("query for " + keyword);
        Request request = new Request.Builder()
                .url("https://www.amazon.com/s?i=aps&k=" + keyword + "&ref=nb_sb_noss&url=search-alias%3Daps")
                .method("GET", null)
                .addHeader("Connection", "keep-alive")
                .addHeader("rtt", "200")
                .addHeader("downlink", "1.5")
                .addHeader("ect", "4g")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("Referer", "https://www.amazon.com/s?k=ipad&ref=nb_sb_noss_1")
                .addHeader("Accept-Language", "en-US,en;q=0.9,he;q=0.8")
                .addHeader("Cookie", "s_fid=4736780042B9999F-0390A4B8EB8FDFC0; aws-priv=eyJ2IjoxLCJldSI6MCwic3QiOjB9; aws-target-static-id=1589374591307-262665; aws-target-data=%7B%22support%22%3A%221%22%7D; aws-target-visitor-id=1589374590616-764874.37_0; aws-ubid-main=858-8232702-6823551; aws-account-alias=itamar-medical; regStatus=registered; session-id=145-0020258-6872618; session-id-time=2082787201l; i18n-prefs=USD; sp-cdn=\"L5Z9:IL\"; ubid-main=134-9698648-9078067; x-wl-uid=1nn3ZmMEoiFFkqReu+paSxZ2AVbTRTSztHzg8kDKtrFk9TzkEunozuSJ0SJg/xP2H1Yuf8MOoklo=; s_vn=1620907567092%26vn%3D9; s_dslv=1590578215532; s_nr=1590578215536-Repeat; aws-userInfo=%7B%22arn%22%3A%22arn%3Aaws%3Aiam%3A%3A831311657046%3Auser%2FNiv%22%2C%22alias%22%3A%22itamar-medical%22%2C%22username%22%3A%22Niv%22%2C%22keybase%22%3A%22%22%2C%22issuer%22%3A%22http%3A%2F%2Fsignin.aws.amazon.com%2Fsignin%22%2C%22signinType%22%3A%22PUBLIC%22%7D; skin=noskin; session-token=9ZO5419FUtyUT1dlaoxH0MUDQDHlbQ9VGwX6TpAGVr2vqcbqlbqMkxSLUmRGZPcKNZsLTPuPINUMWJD/mS71j8x0OF78/cd396ookiwtdwe4zTpecqC9a0Inq3q7Zm5mP/GMYtUh4S50gVI+yx6+1MYDIs0EStAj1UfG28aFVHu13bmBxqjDaNvQxlBmWpkE; csm-hit=tb:M10EKWPRD6DT5MQVG340+s-M10EKWPRD6DT5MQVG340|1591167178737&t:1591167178737&adb:adblk_no")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String process(String keyword) throws IOException {
        String res = "";
        String text = doQuery(keyword);
        Pattern name = Pattern.compile("span class=\"a-size-medium a-color-base a-text-normal\" dir=\"auto\">([A-Za-z0-9\\(\\)\\- ]+)</span>                                </a>    </h2>            </div>");
        Pattern price = Pattern.compile("<span class=\"a-offscreen\">([0-9.$]+)</span><span aria-hidden=\"true\"><span class=\"a-price-symbol\">");
        System.out.println(text.replace("\n", ""));
        String[] prods = (text.replace("\n", "").split("</div></div>        </div>      </div></div>    </div>  </div></div></span>"));
        for (String p : prods) {
            //System.out.println(p);
            Matcher m = name.matcher(p);
            Matcher pr = price.matcher(p);
            if (m.find() && pr.find()) {
                System.out.println(m.group(1) + " price: " + pr.group(1));
                res += m.group(1) + " price: " + pr.group(1);
            }

        }
        return res;
    }
}