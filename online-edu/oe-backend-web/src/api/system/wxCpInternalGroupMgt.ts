import { ContentTypeEnum } from '@/constants';
import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsWxCpInternalGroup/page',
    params,
  });
}
export function findByGroupId(id: any) {
  return request.get<any>({
    url: `/admin/bsWxCpInternalGroup/findByGroupId?groupId=${id}`,
  });
}
