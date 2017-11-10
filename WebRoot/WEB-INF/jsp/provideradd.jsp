<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fm" uri="http://www.springframework.org/tags/form"%>
<%@include file="/WEB-INF/jsp/common/head.jsp"%>

<div class="right">
	<div class="location">
		<strong>你现在所在的位置是:</strong> <span>供应商管理页面 >> 供应商添加页面</span>
	</div>
	<div class="providerAdd">
		<fm:form method="post" modelAttribute="provider">
			<div>
				<label for="proCode">供应商编码：</label>
				<fm:input path="proCode" />
				<!-- 放置提示信息 -->
				<font color="red"></font>
			</div>
			<div>
				<label for="proName">供应商名称：</label>
				<fm:input path="proName" />
				<font color="red"></font>
			</div>
			<div>
				<label for="proContact">联系人：</label>
				<fm:input path="proContact" />
				<font color="red"></font>
			</div>
			<div>
				<label for="proPhone">联系电话：</label>
				<fm:input path="proPhone" />
				<font color="red"></font>
			</div>
			<div>
				<label for="proAddress">联系地址：</label>
				<fm:input path="proAddress" />
			</div>
			<div>
				<label for="proFax">传真：</label>
				<fm:input path="proFax" />
			</div>
			<div>
				<label for="proDesc">描述：</label>
				<fm:input path="proDesc" />
			</div>

			<div class="providerAddBtn">
				<input type="button" name="add" id="add" value="保存"> <input
					type="button" id="back" name="back" value="返回">
			</div>
		</fm:form>
	</div>
</div>
</section>
<%@include file="/WEB-INF/jsp/common/foot.jsp"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/statics/js/provideradd.js"></script>
