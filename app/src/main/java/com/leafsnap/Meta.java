
package com.leafsnap;

import com.google.gson.annotations.Expose;

public class Meta {

    @Expose
    private Long total;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

}
