import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeMemberIdCard/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeMemberIdCard/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeMemberIdCard/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeMemberIdCard/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeMemberIdCard/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeMemberIdCard/delete',
    data,
  });
}

export function findByMemberId(memberId: any) {
  return request.get<any>({
    url: `/admin/oeMemberIdCard/findByMemberId?memberId=${memberId}`,
  });
}
