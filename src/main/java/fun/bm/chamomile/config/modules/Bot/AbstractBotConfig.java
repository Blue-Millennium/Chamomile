package fun.bm.chamomile.config.modules.Bot;

import fun.bm.chamomile.config.ConfigModule;

public abstract class AbstractBotConfig implements ConfigModule {
    @Override
    public String[] category() {
        return new String[]{"bot"};
    }

    @Override
    public abstract String name();
}
