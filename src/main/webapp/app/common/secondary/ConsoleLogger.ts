import type { Logger } from '@/common/domain/Logger';
import type { Message } from '@/common/domain/Message';

export default class ConsoleLogger implements Logger {
  constructor(private readonly logger: Console) {}

  error(message: Message, error: Error) {
    this.logger.error(`${message}\n`, error);
  }
}
