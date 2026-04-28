import { ContentTypeEnum } from '@/constants';
import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsWxCpExternalGroup/page',
    params,
  });
}
export function findByGroupId(id: any) {
  return request.get<any>({
    url: `/admin/bsWxCpExternalGroup/findByGroupId?groupId=${id}`,
  });
}
