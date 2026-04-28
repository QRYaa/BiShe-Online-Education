import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeLesson/page',
    params,
  });
}

export function findByChapterId(chapterId: any) {
  return request.get<any>({
    url: `/admin/oeLesson/findByChapterId?chapterId=${chapterId}`,
  });
}
export function findByChapterIdAndMemberId(chapterId: any, memberId: any) {
  return request.get<any>({
    url: `/admin/oeLesson/findByChapterIdAndMemberId?chapterId=${chapterId}&memberId=${memberId}`,
  });
}

export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeLesson/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeLesson/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeLesson/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeLesson/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeLesson/delete',
    data,
  });
}
