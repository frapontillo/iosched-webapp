<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <style>
        .form-signin { padding: 30px; margin: 0 auto; border: 1px solid #888; border-radius: 10px; }
        .form-signin input { margin-top: 10px; margin-bottom: 10px; }
    </style>
</head>
<body>
<div class="row">&nbsp;</div>
<c:if test="${not empty error}">
    <div class="row"><div class="alert alert-danger text-center">${error}</div></div>
</c:if>

<div class="row">
    <div class="col-md-12">
       <h4>Terms of Service</h4>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
            WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
            PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
            DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
            PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
            HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
            (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
            EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.</p>
        <p><i>(Yup, that's the BSD standard rider)</i></p>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <h4>Registration Details</h4>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <form class="form" role="form" name="registrationdetails" action="<c:url value='/Register'/>" method="POST" accept-charset="UTF-8">
            <label for="humanName">Your Name</label>
            <input type="text" id="humanName" name="name" class="form-control" placeholder="Joe Bloggs" value="${param.name}"/>
            <label for="email">Your Email Address</label>
            <input type="text" id="email" name="email" class="form-control" placeholder="me@somewhere.com" value="${param.email}"/>
            <button type="submit" class="btn btn-lg btn-primary" style="margin-top: 10px">I agree to the Terms of Service, so sign me up</button>
        </form>
    </div>
</div>


</body>
</html>