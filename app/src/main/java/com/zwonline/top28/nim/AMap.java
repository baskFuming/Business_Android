package com.zwonline.top28.nim;

public final class AMap {
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final String CHINESE = "zh_cn";
    public static final String ENGLISH = "en";
    private final IAMap a;
//    private UiSettings b;
//    private Projection c;

    protected AMap(IAMap var1) {
        this.a = var1;
    }

    private IAMap a() {
        return this.a;
    }

//    public final CameraPosition getCameraPosition() {
//        String var1 = "getCameraPosition";
//
//        try {
//            return this.a().getCameraPosition();
//        } catch (RemoteException var3) {
//            ct.a(var3, "AMap", var1);
//            throw new RuntimeRemoteException(var3);
//        }
//    }

    public final float getMaxZoomLevel() {
        return this.a().getMaxZoomLevel();
    }

    public final float getMinZoomLevel() {
        return this.a().getMinZoomLevel();
    }

//    public final void moveCamera(CameraUpdate var1) {
//        String var2 = "moveCamera";
//
//        try {
//            this.a().moveCamera(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }

//    }

//    public final void animateCamera(CameraUpdate var1) {
//        String var2 = "animateCamera";
//
//        try {
//            this.a().animateCamera(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }

//    public final void animateCamera(CameraUpdate var1, AMap.CancelableCallback var2) {
//        String var3 = "animateCamera";
//
//        try {
//            this.a().animateCameraWithCallback(var1, var2);
//        } catch (Throwable var5) {
//            ct.a(var5, "AMap", var3);
//        }
//
//    }

//    public final void animateCamera(CameraUpdate var1, long var2, AMap.CancelableCallback var4) {
//        String var5 = "animateCamera";
//
//        try {
//            if(var2 <= 0L) {
//                Log.w("AMap", "durationMs must be positive");
//            }
//
//            this.a().animateCameraWithDurationAndCallback(var1, var2, var4);
//        } catch (Throwable var7) {
//            ct.a(var7, "AMap", var5);
//        }
//
//    }
//
//    public final void stopAnimation() {
//        String var1 = "stopAnimation";
//
//        try {
//            this.a().stopAnimation();
//        } catch (Throwable var3) {
//            ct.a(var3, "AMap", var1);
//        }
//
//    }

//    public final Polyline addPolyline(PolylineOptions var1) {
//        String var2 = "addPolyline";
//
//        try {
//            return this.a().addPolyline(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//            return null;
//        }
//    }
//
//    public final Text addText(TextOptions var1) {
//        try {
//            return this.a.addText(var1);
//        } catch (Throwable var3) {
//            ct.a(var3, "AMap", "addText");
//            return null;
//        }
//    }
//
//    public final Circle addCircle(CircleOptions var1) {
//        String var2 = "addCircle";
//
//        try {
//            return this.a().addCircle(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//            return null;
//        }
//    }
//
//    public final Polygon addPolygon(PolygonOptions var1) {
//        String var2 = "addPolygon";
//
//        try {
//            return this.a().addPolygon(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//            return null;
//        }
//    }
//
//    public final Marker addMarker(MarkerOptions var1) {
//        String var2 = "addMarker";
//
//        try {
//            return this.a().addMarker(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//            return null;
//        }
//    }
//
//    public final GroundOverlay addGroundOverlay(GroundOverlayOptions var1) {
//        String var2 = "addGroundOverlay";
//
//        try {
//            return this.a().addGroundOverlay(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//            return null;
//        }
//    }
//
//    public final TileOverlay addTileOverlay(TileOverlayOptions var1) {
//        String var2 = "addtileOverlay";
//
//        try {
//            return this.a().addTileOverlay(var1);
//        } catch (RemoteException var4) {
//            ct.a(var4, "AMap", var2);
//            throw new RuntimeRemoteException(var4);
//        }
//    }
//
//    public final void clear() {
//        String var1 = "clear";
//
//        try {
//            if(this.a() != null) {
//                this.a().clear();
//            }
//        } catch (RemoteException var3) {
//            ct.a(var3, "AMap", var1);
//            throw new RuntimeRemoteException(var3);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var1);
//        }
//
//    }
//
//    public final int getMapType() {
//        String var1 = "getMapType";
//
//        try {
//            return this.a().getMapType();
//        } catch (RemoteException var3) {
//            ct.a(var3, "AMap", var1);
//            throw new RuntimeRemoteException(var3);
//        }
//    }
//
//    public final void setMapType(int var1) {
//        String var2 = "setMapType";
//
//        try {
//            this.a().setMapType(var1);
//        } catch (RemoteException var4) {
//            ct.a(var4, "AMap", var2);
//            throw new RuntimeRemoteException(var4);
//        }
//    }
//
//    public final void setMyLocationRotateAngle(float var1) {
//        String var2 = "setMyLocationRoteteAngle";
//
//        try {
//            this.a.setMyLocationRotateAngle(var1);
//        } catch (RemoteException var4) {
//            ct.a(var4, "AMap", var2);
//            throw new RuntimeRemoteException(var4);
//        }
//    }
//
//    public final boolean isTrafficEnabled() {
//        String var1 = "isTrafficEnable";
//
//        try {
//            return this.a().isTrafficEnabled();
//        } catch (RemoteException var3) {
//            ct.a(var3, "AMap", var1);
//            throw new RuntimeRemoteException(var3);
//        }
//    }
//
//    public void setTrafficEnabled(boolean var1) {
//        String var2 = "setTradficEnabled";
//
//        try {
//            this.a().setTrafficEnabled(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public final boolean isMyLocationEnabled() {
//        String var1 = "isMyLocationEnabled";
//
//        try {
//            return this.a().isMyLocationEnabled();
//        } catch (Throwable var3) {
//            ct.a(var3, "AMap", var1);
//            return false;
//        }
//    }
//
//    /** @deprecated */
//    public static String getVersion() {
//        return "3.0.0";
//    }
//
//    public final void setMyLocationEnabled(boolean var1) {
//        String var2 = "setMyLocationEnabled";
//
//        try {
//            this.a().setMyLocationEnabled(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public final Location getMyLocation() {
//        String var1 = "getMyLocation";
//
//        try {
//            return this.a().getMyLocation();
//        } catch (Throwable var3) {
//            ct.a(var3, "AMap", var1);
//            return null;
//        }
//    }
//
//    public final void setLocationSource(LocationSource var1) {
//        String var2 = "setLocationSource";
//
//        try {
//            this.a().setLocationSource(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public final void setMyLocationStyle(MyLocationStyle var1) {
//        String var2 = "setMyLocationStyle";
//
//        try {
//            this.a().setMyLocationStyle(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public final UiSettings getUiSettings() {
//        String var1 = "getUiSettings";
//
//        try {
//            if(this.b == null) {
//                this.b = this.a().getAMapUiSettings();
//            }
//
//            return this.b;
//        } catch (Throwable var3) {
//            ct.a(var3, "AMap", var1);
//            return null;
//        }
//    }
//
//    public final Projection getProjection() {
//        String var1 = "getProjection";
//
//        try {
//            if(this.c == null) {
//                this.c = this.a().getAMapProjection();
//            }
//
//            return this.c;
//        } catch (Throwable var3) {
//            ct.a(var3, "AMap", var1);
//            return null;
//        }
//    }
//
//    public final void setOnCameraChangeListener(AMap.OnCameraChangeListener var1) {
//        String var2 = "setOnCameraChangeListener";
//
//        try {
//            this.a().setOnCameraChangeListener(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public final void setOnMapClickListener(AMap.OnMapClickListener var1) {
//        String var2 = "setOnMapClickListener";
//
//        try {
//            this.a().setOnMapClickListener(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public final void setOnMapTouchListener(AMap.OnMapTouchListener var1) {
//        String var2 = "setOnMapTouchListener";
//
//        try {
//            this.a.setOnMapTouchListener(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public final void setOnMyLocationChangeListener(AMap.OnMyLocationChangeListener var1) {
//        String var2 = "setOnMyLocaitonChangeListener";
//
//        try {
//            this.a().setOnMyLocationChangeListener(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public final void setOnMapLongClickListener(AMap.OnMapLongClickListener var1) {
//        String var2 = "setOnMapLongClickListener";
//
//        try {
//            this.a().setOnMapLongClickListener(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public final void setOnMarkerClickListener(AMap.OnMarkerClickListener var1) {
//        String var2 = "setOnMarkerClickListener";
//
//        try {
//            this.a().setOnMarkerClickListener(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public final void setOnMarkerDragListener(AMap.OnMarkerDragListener var1) {
//        String var2 = "setOnMarkerDragListener";
//
//        try {
//            this.a().setOnMarkerDragListener(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public final void setOnInfoWindowClickListener(AMap.OnInfoWindowClickListener var1) {
//        String var2 = "setOnInfoWindowClickListener";
//
//        try {
//            this.a().setOnInfoWindowClickListener(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public final void setInfoWindowAdapter(AMap.InfoWindowAdapter var1) {
//        String var2 = "setInfoWindowAdapter";
//
//        try {
//            this.a().setInfoWindowAdapter(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public final void setOnMapLoadedListener(AMap.OnMapLoadedListener var1) {
//        String var2 = "setOnMapLoadedListener";
//
//        try {
//            this.a().setOnMaploadedListener(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public void getMapScreenShot(AMap.OnMapScreenShotListener var1) {
//        this.a().getMapScreenShot(var1);
//        this.invalidate();
//    }
//
//    public float getScalePerPixel() {
//        return this.a().getScalePerPixel();
//    }
//
//    public final List<Marker> getMapScreenMarkers() {
//        String var1 = "getMapScreenaMarkers";
//
//        try {
//            return this.a.getMapScreenMarkers();
//        } catch (Throwable var3) {
//            ct.a(var3, "AMap", var1);
//            return null;
//        }
//    }
//
//    public void postInvalidate() {
//        this.a().AMapInvalidate();
//    }
//
//    public void invalidate() {
//        this.postInvalidate();
//    }
//
//    public void setMapLanguage(String var1) {
//        String var2 = "setMapLanguage";
//
//        try {
//            this.a.setMapLanguage(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public void removecache() {
//        String var1 = "removecache";
//
//        try {
//            this.a.removecache();
//        } catch (Throwable var3) {
//            ct.a(var3, "AMap", var1);
//        }
//
//    }
//
//    public void removecache(AMap.OnCacheRemoveListener var1) {
//        String var2 = "removecache";
//
//        try {
//            this.a.removecache(var1);
//        } catch (Throwable var4) {
//            ct.a(var4, "AMap", var2);
//        }
//
//    }
//
//    public interface OnCacheRemoveListener {
//        void onRemoveCacheFinish(boolean var1);
//    }
//
//    public interface OnMapScreenShotListener {
//        void onMapScreenShot(Bitmap var1);
//    }
//
//    public interface OnMapLoadedListener {
//        void onMapLoaded();
//    }
//
//    public interface OnMapTouchListener {
//        void onTouch(MotionEvent var1);
//    }
//
//    public interface OnMapClickListener {
//        void onMapClick(LatLng var1);
//    }
//
//    public interface OnMapLongClickListener {
//        void onMapLongClick(LatLng var1);
//    }
//
//    public interface OnCameraChangeListener {
//        void onCameraChange(CameraPosition var1);
//
//        void onCameraChangeFinish(CameraPosition var1);
//    }
//
//    public interface OnMarkerClickListener {
//        boolean onMarkerClick(Marker var1);
//    }
//
//    public interface OnMarkerDragListener {
//        void onMarkerDragStart(Marker var1);
//
//        void onMarkerDrag(Marker var1);
//
//        void onMarkerDragEnd(Marker var1);
//    }
//
//    public interface OnInfoWindowClickListener {
//        void onInfoWindowClick(Marker var1);
//    }
//
//    public interface CancelableCallback {
//        void onFinish();
//
//        void onCancel();
//    }
//
//    public interface OnMyLocationChangeListener {
//        void onMyLocationChange(Location var1);
//    }
//
//    public interface InfoWindowAdapter {
//        View getInfoWindow(Marker var1);
//
//        View getInfoContents(Marker var1);
//    }
}
