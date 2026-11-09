package com.B2BMarket.ordering.application.utility;




public interface Mapper {
    <T> T convert(Object object, Class<T> destinationType);
}
