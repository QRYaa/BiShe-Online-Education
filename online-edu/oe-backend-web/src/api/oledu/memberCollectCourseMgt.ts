import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeMemberCollectCourse/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeMemberCollectCourse/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeMemberCollectCourse/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeMemberCollectCourse/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeMemberCollectCourse/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeMemberCollectCourse/delete',
    data,
  });
}
