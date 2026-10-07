package com.junkfood.seal.ui.component

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp

private const val ThreeJsSourceUrl =
    "https://cdn.jsdelivr.net/npm/three@0.150.1/build/three.min.js"

private val PineappleHeroScript =
    """
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
<style>
html,body{margin:0;padding:0;background:transparent;overflow:hidden;}
#threejs-container{width:100%;height:100%;}
</style>
</head>
<body>
<div id="threejs-container"></div>
<script src="$ThreeJsSourceUrl"></script>
<script>
(function () {
  var container = document.getElementById('threejs-container');
  var width = container.clientWidth || 400;
  var height = container.clientHeight || 300;
  var scene = new THREE.Scene();
  var camera = new THREE.PerspectiveCamera(75, width / height, 0.1, 1000);
  var renderer = new THREE.WebGLRenderer({ alpha: true, antialias: true });
  renderer.setSize(width, height);
  renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
  container.appendChild(renderer.domElement);

  var group = new THREE.Group();
  scene.add(group);

  var bodyGeo = new THREE.CylinderGeometry(1, 1.2, 2.5, 32);
  var bodyMat = new THREE.MeshPhongMaterial({ color: 0xFFD700, emissive: 0x332200, shininess: 60 });
  var pineapple = new THREE.Mesh(bodyGeo, bodyMat);
  group.add(pineapple);

  var diamondGeo = new THREE.OctahedronGeometry(0.14, 0);
  var diamondMat = new THREE.MeshPhongMaterial({ color: 0x6B5400, emissive: 0x1A1400, shininess: 90 });
  for (var i = 0; i < 26; i++) {
    var theta = Math.random() * Math.PI * 2;
    var y = (Math.random() - 0.5) * 2.2;
    var r = 1.02 + Math.random() * 0.16;
    var d = new THREE.Mesh(diamondGeo, diamondMat);
    d.position.set(Math.cos(theta) * r, y, Math.sin(theta) * r);
    d.rotation.set(Math.random() * Math.PI, Math.random() * Math.PI, Math.random() * Math.PI);
    group.add(d);
  }

  var leafGeo = new THREE.ConeGeometry(0.3, 1, 8);
  var leafMat = new THREE.MeshPhongMaterial({ color: 0x32CD32, emissive: 0x052205, shininess: 40 });
  var crown = new THREE.Group();
  for (var j = 0; j < 7; j++) {
    var leaf = new THREE.Mesh(leafGeo, leafMat);
    var angle = (j / 7) * Math.PI * 2;
    leaf.position.set(Math.cos(angle) * 0.42, 1.5, Math.sin(angle) * 0.42);
    leaf.rotation.set(0.35, -angle, 0.25);
    crown.add(leaf);
  }
  group.add(crown);

  var ring = new THREE.Mesh(
    new THREE.TorusGeometry(2.1, 0.012, 8, 96),
    new THREE.MeshBasicMaterial({ color: 0xFFD700, transparent: true, opacity: 0.34 })
  );
  ring.rotation.x = Math.PI / 2;
  ring.position.y = -0.15;
  group.add(ring);

  var ring2 = new THREE.Mesh(
    new THREE.TorusGeometry(2.6, 0.008, 8, 96),
    new THREE.MeshBasicMaterial({ color: 0x32CD32, transparent: true, opacity: 0.22 })
  );
  ring2.rotation.x = Math.PI / 2.35;
  ring2.rotation.z = 0.35;
  ring2.position.y = -0.35;
  group.add(ring2);

  var circuitMat = new THREE.LineBasicMaterial({ color: 0xFFD700, transparent: true, opacity: 0.42 });
  var circuits = [];
  for (var k = 0; k < 9; k++) {
    var ang = (k / 9) * Math.PI * 2;
    var pts = [];
    pts.push(new THREE.Vector3(Math.cos(ang) * 1.15, (Math.random() - 0.5) * 2.0, Math.sin(ang) * 1.15));
    pts.push(new THREE.Vector3(Math.cos(ang) * 1.7, (Math.random() - 0.5) * 2.4, Math.sin(ang) * 1.7));
    pts.push(new THREE.Vector3(Math.cos(ang) * 2.5, (Math.random() - 0.5) * 2.9, Math.sin(ang) * 2.5));
    var line = new THREE.Line(new THREE.BufferGeometry().setFromPoints(pts), circuitMat.clone());
    circuits.push(line);
    group.add(line);
  }

  scene.add(new THREE.AmbientLight(0xffffff, 0.5));

  var dirLight = new THREE.DirectionalLight(0xFFD700, 1);
  dirLight.position.set(5, 5, 5);
  scene.add(dirLight);

  var rimLight = new THREE.DirectionalLight(0x32CD32, 0.75);
  rimLight.position.set(-5, 1.5, -4);
  scene.add(rimLight);

  camera.position.z = 6.2;

  var targetX = 0;
  var targetY = 0;
  var pointerX = 0;
  var pointerY = 0;

  function onPointer(x, y) {
    targetX = (x / window.innerWidth) - 0.5;
    targetY = (y / window.innerHeight) - 0.5;
  }

  document.addEventListener('touchstart', function (event) {
    if (event.touches.length > 0) onPointer(event.touches[0].clientX, event.touches[0].clientY);
  }, { passive: true });

  document.addEventListener('touchmove', function (event) {
    if (event.touches.length > 0) onPointer(event.touches[0].clientX, event.touches[0].clientY);
  }, { passive: true });

  document.addEventListener('mousemove', function (event) {
    onPointer(event.clientX, event.clientY);
  }, { passive: true });

  function animate() {
    requestAnimationFrame(animate);
    pointerX += (targetX - pointerX) * 0.05;
    pointerY += (targetY - pointerY) * 0.05;
    group.rotation.y += 0.0035 + pointerX * 0.006;
    group.rotation.x += pointerY * 0.004;
    group.position.y = Math.sin(Date.now() * 0.001) * 0.2;
    ring.rotation.z += 0.002;
    ring2.rotation.z -= 0.0025;
    for (var c = 0; c < circuits.length; c++) {
      circuits[c].material.opacity = 0.22 + 0.3 * (0.5 + 0.5 * Math.sin(Date.now() * 0.0018 + c));
    }
    renderer.render(scene, camera);
  }
  animate();
})();
</script>
</body>
</html>
"""
    .trimIndent()

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PineappleHero3D(modifier: Modifier = Modifier, height: Int = 240) {
    val context = LocalContext.current
    val webView =
        remember {
            WebView(context).apply {
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = false
                isVerticalScrollBarEnabled = false
                isHorizontalScrollBarEnabled = false
                webViewClient = WebViewClient()
                loadDataWithBaseURL(
                    "https://localhost/",
                    PineappleHeroScript,
                    "text/html",
                    "utf-8",
                    null,
                )
            }
        }

    AndroidView(
        modifier = modifier.fillMaxWidth().height(height.dp),
        factory = { webView },
    )
}
