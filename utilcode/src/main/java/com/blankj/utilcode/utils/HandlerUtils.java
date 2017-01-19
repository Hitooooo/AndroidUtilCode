package com.blankj.utilcode.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 16/11/01
 *     desc  : Handler相关工具类
 *
 *     解决直接在Activity内部new对象导致的内存泄漏问题,其实如果未处理的消息处理完之后,activity还是可以被回收的.
 *      描述:由于Handler匿名内部类会泄漏(内部类或匿名内部类会持有外部引用) 解决方法就是将内部类声明为静态,但是如此以来
 *      静态的Handler无法使用Activity中的成员变量,因为静态方法只能访问静态成员变量和方法.
 *
 *      综上:为了保证Handler不会持有Activity的引用,同时可以轻松使用Activity中的成员变量.
 *          1,将Handler设为静态
 *          2,通过接口的方式将Activity并且弱引用的方式,绑定Activity
 *
 * </pre>
 */
public class HandlerUtils {

    private HandlerUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static class HandlerHolder extends Handler {
        WeakReference<OnReceiveMessageListener> mListenerWeakReference;

        /**
         * 使用必读：推荐在Activity或者Activity内部持有类中实现该接口，不要使用匿名类，可能会被GC
         * 因为使用了弱引用,匿名内部类会被回收,但是如果是Activity的话,并且处于前台,不会被系统回收
         *
         * @param listener 收到消息回调接口
         */
        public HandlerHolder(OnReceiveMessageListener listener) {
            mListenerWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mListenerWeakReference != null && mListenerWeakReference.get() != null) {
                mListenerWeakReference.get().handlerMessage(msg);
            }
        }
    }

    /**
     * 收到消息回调接口
     */
    public interface OnReceiveMessageListener {
        void handlerMessage(Message msg);
    }
}
