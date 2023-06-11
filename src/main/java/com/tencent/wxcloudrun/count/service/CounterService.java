package com.tencent.wxcloudrun.count.service;

import com.tencent.wxcloudrun.count.model.Counter;

import java.util.Optional;

public interface CounterService {

  Optional<Counter> getCounter(Integer id);

  void upsertCount(Counter counter);

  void clearCount(Integer id);
}
