package com.ttvnp.ttj_asset_android_client.domain.model

class ModelWrapper<T : BaseModel?>(val model: T? = null, val errorCode: ErrorCode = ErrorCode.NO_ERROR)