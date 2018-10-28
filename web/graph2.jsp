

<%@page import="java.util.Random"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!
    String displayName;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="stylesheets/jquery-ui.min.css"/>
        <link type="text/css" href="stylesheets/newcss.css" rel="stylesheet">
        <link type="text/css" href="stylesheets/formcss.css" rel="stylesheet">

        <script src="scripts/jquery-1.10.2.js"></script>
        <script src="scripts/jquery-ui.min.js"></script>
        <title>Graph</title>
    </head>
    <script>
        $(document).ready(function () {
            $('#user-div-id').click(function () {
                $('#logout-div').toggle('slow');
            });
            $('#logout-div').click(function () {
                location.href = 'logout.html';
            });
        });
    </script>
    <body>
        <%
            displayName = (String) session.getAttribute("dname");
            if (displayName == null || displayName.equalsIgnoreCase("")) {
                response.sendRedirect("index.jsp");
            }
        %>
        <div id="abs-dev">
            <div class="header-div">
                <div class="logo-div">
                    <img src="img/logo.png">
                </div>
                <div class="name-div">
                    <img src="img/formplusplus.png">
                </div>
                <div class="user-div" id="user-div-id">
                    <div style="float: left; padding: 1%; width: 250px; ">
                        Welcome <b><%=displayName%></b>
                    </div>
                    <br>
                    <div id="logout-div" hidden>
                        Logout
                    </div>
                </div>
            </div>
        </div>
        <%!
            ArrayList<ArrayList<String>> tab = new ArrayList<ArrayList<String>>();

            String xoriant;

            HashMap<String, Integer> count = new HashMap<String, Integer>();
            //ArrayList<Object>
            ArrayList<String> att = new ArrayList<String>();
            ArrayList<Integer> mAll;
            ArrayList<String> m;
            Double rot = 0.0, totalN = 0.0;
            String currCol = "";
            int t = 0, b = 0, w = 40, h = 0, tempH = 0, l = 0, topScore = 0, avg = 0, consty = 500, multiK = 20, leftMar = 200, gap = 20, xaxisLimit = 0, ind = 0;
            int maxN = 0, i = 0;
            String yLabel, xLabel;

            public String randColor() {
                Random r = new Random();
                int rI = r.nextInt(888888) + 100000;
                int rC = 999999 - rI;
                return "#" + rI;
            }
        %>


        <%
           // att.add("name");
            // att.add("marks");
            tab.clear();

            tab = (ArrayList<ArrayList<String>>) request.getSession().getAttribute("tab");
            att = (ArrayList<String>) request.getSession().getAttribute("att");
            int index = Integer.parseInt(request.getParameter("ind"));

            for (ArrayList elem : tab) {

                String tempM = elem.get(index).toString();

                if (count.containsKey(tempM)) {
                    count.put(tempM, count.get(tempM) + 1);
                } else {
                    count.put(tempM, 1);
                }
            }
            xLabel = att.get(index);
            yLabel = "No of Entries";


        %>


        <style>
            .ything{

                font-size: larger;
                font-style: oblique;
                transform: rotate(270deg);

                left:<%=leftMar - 140%>px;
                top: <%=consty - 120%>px;
                position: absolute;
            }
            .xthing{

                font-size: larger;
                font-style: oblique;


                left:<%=leftMar + 80%>px;
                top: <%=consty + 25%>px;
                position: absolute;

            } 
        </style>
        <div class="ything" ><%=yLabel%></div>
        <div class="xthing"><%=xLabel%></div>



        <!-- yaxis-->


        <%
            mAll = new ArrayList<Integer>();
            for (Map.Entry<String, Integer> entry : count.entrySet()) {
                if (maxN < entry.getValue()) {
                    maxN = entry.getValue();
                }

                totalN += entry.getValue();
            }

            for (int tp = 0; tp < maxN + 3; tp++) {
                mAll.add(tp);
            }

            for (ind = 0; ind < mAll.size(); ind += 5) {
            //out.println(m.get(ind));

        %>
        <style>

            .yLab<%=ind%>{

                left: <%=leftMar - 70%>px;
                top: <%=consty - (multiK) * ind - 8%>px;
                position:absolute;
            }
        </style>


        <div class='yLab<%=ind%>'> <%=mAll.get(ind)%></div>
        <% }%>

        <style>
            .yaxis{
                width: 2px;
                height: <%=maxN * multiK + 20%>px;
                background-color: black;
                position: absolute;
                top: <%=consty - maxN * multiK - 20%>px;
                left: <%=leftMar - 30%>px;

            }



        </style>
        <div class="yaxis"></div>


        <!-- xaxis-->
        <%
            i = 0;

            m = new ArrayList<String>();
            m.clear();
            for (Map.Entry<String, Integer> entry : count.entrySet()) {

                m.add(entry.getKey());
            }

            class comp implements Comparator<String> {

                public int compare(String a, String b) {
                    if (a.compareTo(b) > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
            //Collections.sort(scores, new comp());
            Collections.sort(m, new comp());

            for (ind = 0; ind < m.size(); ind++) {
                //out.println(m.get(ind));
                xaxisLimit = (3 + ind) * (w + gap);
        %>
        <style>

            .xLab<%=ind%>{

                left: <%=leftMar + ind * (w + gap)%>px;
                top: <%=consty + 5%>px;
                position:absolute;
            }
        </style>
        <div class="xLab<%=ind%>"><%=m.get(ind)%></div>
        <%}%>
        <style>
            .xaxis{
                width: <%=xaxisLimit%>px;
                height: 2px;
                background-color: black;
                position: absolute;
                top: <%=consty%>px;
                left: <%=leftMar - 30%>px;

            }
        </style>
        <div class="xaxis"></div>
        <!-- bars -->


        <%
            h = 0;

            for (String d : m) {
                //Double h2;
                tempH = h = count.get(d);
                //h=multiK*entry.getValue();
                h = multiK * h;
                t = consty - h;
                l = leftMar + i * (w + gap);
                /*if(i==0)
                 rot = 0.0;
                 else
                 rot+= 360.0 * (count.get(d)/totalN);*/
        %>
        <style>
            .verbar<%=i%> {
                width: <%=w%>px;
                height: <%=h%>px;
                background-color: coral;
                position: absolute;
                top: <%=t%>px;
                left: <%=l%>px;
                transition: width 2s;
            }
            .bar<%=i%> {
                width: <%=w%>px;
                height: <%=h%>px;
                background-color: coral;
                position: absolute;
                top: <%=t%>px;
                left: <%=l%>px;
                transition: width 2s;
            }

            .verbar<%=i%>:hover > #LabelB<%=i%>{visibility: visible;}


            #LabelB<%=i%>{visibility: hidden;}

        </style>

        <div class = "verbar<%=i%>" id='bar<%=i%>' onclick="show(<%=i%>,<%=m.size()%>)" ondblclick="hide(<%=m.size()%>)" onmouseout="hide(<%=m.size()%>)"><div id='LabelB<%=i%>' >
                <%=tempH%>

            </div>
        </div>

        <%System.out.print(i + " i");

                i++;
            }

        %>
        <canvas id="tp" width="300" height="300" style="position: absolute; left:800px;"></canvas>  
        <canvas id="rect" width="300" height="800" style="position: absolute; left:800px; top:-150px;"></canvas>  
        <script>
            //var datalist = [];// = new Array();
            //var colist = [];// = new Array();

            function assign()
            {



            }

            function pie(ctx, w, h, datalist)
            {


                //assign();


                var radius = h / 2 - 5;
                var centerx = w / 2;
                var centery = h / 2;
                var total = 0;
                for (x = 0; x < datalist.length; x++) {
                    total += datalist[x];
                    //alert(datalist[x]);
                }
                ;
                var lastend = 0;
                var offset = Math.PI / 2;
                for (x = 0; x < datalist.length; x++)
                {
                    var thispart = datalist[x];
                    ctx.beginPath();
                    ctx.fillStyle = colist[x];
                    ctx.moveTo(centerx, centery);
                    var arcsector = Math.PI * (2 * thispart / total);
                    ctx.arc(centerx, centery, radius, lastend - offset, lastend + arcsector - offset, false);
                    ctx.lineTo(centerx, centery);
                    ctx.fill();
                    ctx.closePath();
                    lastend += arcsector;
                }
            }

            var datalist = [];
            var colist = [];
            //alert('xxxxx' + m.size());
            <%                for (String d : m) {
                    xoriant = d;
            %>
            //alert('<%=xoriant%>');
            datalist[datalist.length] = <%=count.get(xoriant)%>;
            colist[colist.length] = '<%=randColor()%>';
            //datalist.push(<%=count.get(d)%>);
            //colist.push(<%=randColor()%>);
            <%
                }
            %>
            //alert(datalist[3] + 'ggg');

            //datalist[datalist.length] = 30;
            //colist[colist.length] = 'black';
            var canvas = document.getElementById("tp");
            var ctx = canvas.getContext('2d');
            var ele = document.getElementById('rect');
            var ctx2 = ele.getContext('2d');

            pie(ctx, canvas.width, canvas.height, datalist);
            sq(ctx2, ele.width, colist);
            function sq(ctx2, side, colist)
            {
                var xxxx = 300;
                for (var iii = 0; iii < colist.length; iii++)
                {
                    xxxx += (side + 5);
                    ctx2.fillStyle = colist[iii];
                    ctx2.fillRect(0, xxxx, 20, 20);
                    ctx2.fillText(datalist[iii], 30, xxxx + 12);


                }

            }

        </script>


        <%
            m.clear();
            count.clear();
            rot = 0.0;
            totalN = 0.0;

        %>