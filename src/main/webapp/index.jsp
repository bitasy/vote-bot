<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.google.cloud.dialogflow.v2beta1.*" %>
<html>
<head>
  <link href='//fonts.googleapis.com/css?family=Marmelad' rel='stylesheet' type='text/css'>
  <title>Hello App Engine Standard Java 8</title>
</head>
<body>
    <img src="resources/VoteBot2.png" about="Vote Bot" style="width: 300px; height: 300px;"/>

    <form action = "/" method = "POST">
      <input type = "text" name = "query"/>
      <input type = "submit" name = "send_query" value = "Send" />
    </form>


    <p>
      <%
        if(request.getParameter("send_query") != null){
          try (SessionsClient sessionsClient = SessionsClient.create()) {
            SessionName dfsession = SessionName.of("vote-assist-chat", "12331");
            TextInput.Builder textInput = TextInput.newBuilder().setText(request.getParameter("query")).setLanguageCode("en");
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
            DetectIntentResponse dfresponse = sessionsClient.detectIntent(dfsession.toString(), queryInput);

            QueryResult result = dfresponse.getQueryResult();

            out.append(result.getFulfillmentText());

          }
        }



      %>
    </p>

    <p>Submit a query to our database.</p>
    <form action = "/sql" method = "GET">
        <input type = "text" name = "query" style="width: 300px">
        <input type = "submit" value = "Send SQL Query" />
    </form>

</body>
</html>
