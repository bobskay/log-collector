package wang.wangby.logtask.parser;

import wang.wangby.logtask.model.LogParseResult;

public interface LogParser<T> {

    LogParseResult<T> parse(Long begin, String text, boolean isEnd) throws Exception;
}
