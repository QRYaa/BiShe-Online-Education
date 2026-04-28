import { ContentTypeEnum } from '@/constants';
import { request } from '@/utils/request';

export function init() {
  return request.post<any>({
    url: '/admin/bsWxCpUser/init',
  });
}

export function push() {
  return request.post<any>({
    url: '/admin/bsWxCpUser/push',
  });
}
