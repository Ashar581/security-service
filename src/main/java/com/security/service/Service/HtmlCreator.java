package com.security.service.Service;

import org.springframework.stereotype.Service;

@Service
public class HtmlCreator {
    public String getHtml(String name){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Login Notification</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            color: #333;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            width: 100%;\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            padding: 10px;\n" +
                "            border-top-left-radius: 8px;\n" +
                "            border-top-right-radius: 8px;\n" +
                "        }\n" +
                "        .content {\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            font-size: 12px;\n" +
                "            color: #777;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Login Notification</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Dear "+name+",</p>\n" +
                "            <p>We noticed that you just logged in to your account. If this was you, you can safely ignore this email.</p>\n" +
                "            <p>If you did not log in, please reset your password immediately and contact our support team.</p>\n" +
                "            <p>Thank you,<br>Securellance</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>&copy; 2024 Securellance. All rights reserved.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }
    public String sendSOS(String name, String receiverName, Double longitude, Double latitude, String sosLocation){
        // Set the actual HTML message
        return  "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <title>Emergency Notification</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; margin: 20px; }" +
                "        .container { max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ccc; border-radius: 10px; background-color: #f9f9f9; }" +
                "        .header { background-color: #ff4d4d; color: white; padding: 10px; text-align: center; border-radius: 10px 10px 0 0; }" +
                "        .map { width: 100%; height: auto; margin: 20px 0; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h2>Emergency Notification</h2>" +
                "        </div>" +
                "        <p>Dear " + receiverName + ",</p>" +
                "        <p>This is an emergency notification. Please find the current location below:</p>" +
                "        <p>Please find my live location from this link: <a href=\"" + sosLocation + "\">click here</a></p>" +
                "        <div>" +
                "            <img class=\"map\" src=\"https://static-maps.yandex.ru/1.x/?lang=en-US&ll=" + longitude + "," + latitude + "&size=600,300&z=15&l=map&pt=" + longitude + "," + latitude + ",pm2rdm\" alt=\"Current Location\">" +
                "        </div>" +
                "        <p>Thank you for your immediate attention to this matter.</p>" +
                "        <p>Best regards,</p>" +
                "        <p>" + name + "</p>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}
