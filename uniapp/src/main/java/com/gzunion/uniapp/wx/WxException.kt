package com.gzunion.uniapp.wx

import com.tencent.mm.opensdk.modelbase.BaseResp

class WxException(resp: BaseResp) : Exception(
    "error from wechat, code:${resp.errCode}, msg:${resp.errStr}"
)