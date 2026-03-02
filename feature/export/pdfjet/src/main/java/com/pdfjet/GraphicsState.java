package com.pdfjet;


public class GraphicsState {

    // Default values
    private float CA = 1f;
    private float ca = 1f;

    public void set_CA(float CA) {
        if (CA >= 0f && CA <= 1f) {
            this.CA = CA;
        }
    }

    public float get_CA() {
        return this.CA;
    }

    public void set_ca(float ca) {
        if (ca >= 0f && ca <= 1f) {
            this.ca = ca;
        }
    }

    public float get_ca() {
        return this.ca;
    }

}
