<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Person信息</title>
</head>
<body>
  <table border="1" >
     <tr>
       <th>Name</th>
       <th>Password</th>
     </tr>
     <tr>
       <td>${user.username }</td>
       <td>${user.password }</td>
     </tr>
  </table>

</body>
</html>