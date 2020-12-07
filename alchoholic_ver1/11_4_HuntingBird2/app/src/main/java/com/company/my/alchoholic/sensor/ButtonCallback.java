package com.company.my.alchoholic.sensor;

public interface ButtonCallback {
    /**
     * 버튼값을 지속적으로 받아오는 메소드
     * 0또는 1이 값이 넘어와진다.
     * @param value 버튼이 눌렸으면 1 안눌렸으면 0
     */
    public void onButtonClick(int value);

}
