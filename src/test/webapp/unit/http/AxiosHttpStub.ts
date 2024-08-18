import sinon, { type SinonStub } from 'sinon';
import { AxiosHttp, type AxiosHttpResponse } from '@/http/AxiosHttp';

export interface AxiosHttpStub extends AxiosHttp {
  get: SinonStub;
  post: SinonStub;
  delete: SinonStub;
  put: SinonStub;
}

export const stubAxiosHttp = (): AxiosHttpStub =>
  ({
    get: sinon.stub(),
    post: sinon.stub(),
    delete: sinon.stub(),
    put: sinon.stub(),
  }) as AxiosHttpStub;

export const dataBackendResponse = <T>(data: T): AxiosHttpResponse<T> =>
  ({
    data,
  }) as AxiosHttpResponse<T>;
