package com.yuanwang.common.core;

import java.io.Serializable;

/**id接口
 * @author crj
 *
 */
public interface IdKey extends Serializable{
    /**获取id
    * @return id
    */
    public Integer getId();

    /**设置id
    * @param id
    */
    public void setId(Integer id);
}
