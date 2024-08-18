import sinon, { type SinonStub } from 'sinon';
import type { Logger } from '@/common/domain/Logger';

export interface LoggerFixture extends Logger {
  error: SinonStub;
}

export const stubLogger = (): LoggerFixture => ({
  error: sinon.stub(),
});
