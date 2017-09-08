package com.wzes.huddle.adapter;

import com.youth.banner.listener.OnBannerListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class EventAdapter$HeaderViewHolder$$Lambda$1 implements OnBannerListener {
    private final HeaderViewHolder arg$1;

    private EventAdapter$HeaderViewHolder$$Lambda$1(HeaderViewHolder headerViewHolder) {
        this.arg$1 = headerViewHolder;
    }

    private static OnBannerListener get$Lambda(HeaderViewHolder headerViewHolder) {
        return new EventAdapter$HeaderViewHolder$$Lambda$1(headerViewHolder);
    }

    public static OnBannerListener lambdaFactory$(HeaderViewHolder headerViewHolder) {
        return new EventAdapter$HeaderViewHolder$$Lambda$1(headerViewHolder);
    }

    @Hidden
    public void OnBannerClick(int i) {
        this.arg$1.lambda$new$0(i);
    }
}
