import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeMemberLesson/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeMemberLesson/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeMemberLesson/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeMemberLesson/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeMemberLesson/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeMemberLesson/delete',
    data,
  });
}
