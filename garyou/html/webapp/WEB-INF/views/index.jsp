<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>garyou</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link href="styles.css" rel="stylesheet" type="text/css">
<script src="soundmanager2-setup.js"></script>
<script src="soundmanager2-jsmin.js"></script>
<script src="https://skyway.io/dist/v2/0.3/peer.js"></script>
</head>

<body>
	<div class="container" id="embed-html"></div>
	<script type="text/javascript" src="html/html.nocache.js"></script>
</body>

<script>
	function handleMouseDown(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		evt.target.style.cursor = 'default';
	}

	function handleMouseUp(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		evt.target.style.cursor = '';
	}
	document.getElementById('embed-html').addEventListener('mousedown',
			handleMouseDown, false);
	document.getElementById('embed-html').addEventListener('mouseup',
			handleMouseUp, false);
</script>
</html>
