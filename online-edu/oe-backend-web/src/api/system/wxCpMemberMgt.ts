import { ContentTypeEnum } from '@/constants';
import { request } from '@/utils/request';

export function memberList() {
  return request.get<any>({
    url: '/admin/bsWxCpMember/memberList',
  });
}
export function chatMembers(chatterId: any) {
  return request.get<any>({
    url: `/admin/bsWxCpMember/chatMembers?chatterId=${chatterId}`,
  });
}

export function findByMemberId(memberId: any) {
  return request.get<any>({
    url: `/admin/bsWxCpMember/findByMemberId?memberId=${memberId}`,
  });
}
