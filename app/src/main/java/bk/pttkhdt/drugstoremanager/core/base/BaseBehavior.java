package bk.pttkhdt.drugstoremanager.core.base;

public interface BaseBehavior {

    /**
     *  Hàm này để load View lên Activity
     */
    void onCommonViewLoaded();

    /**
     *  Hàm này để xử lý các sự kiện thao tác của người dùng
     */
    void addViewListener();

    /**
     *  Hàm này để lắng nghe sự thay đổi của dữ liệu
     */
    void addDataObserve();

    /**
     * show or hide loading
     */
    void onLoading(Boolean isLoading);
}
