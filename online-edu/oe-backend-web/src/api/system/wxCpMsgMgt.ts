import { ContentTypeEnum } from '@/constants';
import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsWxCpMsg/page',
    params,
  });
}
